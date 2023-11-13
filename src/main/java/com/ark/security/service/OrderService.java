package com.ark.security.service;

import com.ark.security.dto.OrderDetailDto;
import com.ark.security.dto.OrderDto;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.payment.*;
import com.ark.security.models.user.User;
import com.ark.security.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final Environment env;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final String MOMO_API = "https://test-payment.momo.vn/v2/gateway/api";


    public Order getOrderById(Integer id){
        return orderRepository.findById(id).orElse(null);
    }

    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(int id){
        return orderRepository.findAllByUserId(id).orElse(null);
    }

    public Order createOrder(User user, Order order){
        Cart cart = cartService.getCartByUsername(user.getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        order.setOrderCode(RandomStringUtils.random(10, true, true));
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItem ci : cartItems){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(ci.getProductDetail());
            orderDetail.setQuantity(ci.getQuantity());
            orderDetail.setPrice(ci.getPrice());
            orderDetail.setTotal(ci.getTotal());
            orderDetail.setOrderDate(LocalDateTime.now());
            orderDetails.add(orderDetail);
            orderDetailService.saveOrderDetail(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        cart.setStatus(false);
        cartService.saveCart(cart);
        return order;
    }

    public MomoResponse createMomoOrder(OrderDto orderDto){
        String endpoint = MOMO_API + "/create";
        MomoRequest momoRequest = momoRequest(orderDto);
        MomoResponse response = processMomoOrder(endpoint, momoRequest);
        if(response.getResultCode() == 0){
            Order order = orderDto.convertToEntity();
            order.setStatus(OrderStatus.PAID_MOMO);
        }
        return response;
    }


    private MomoResponse processMomoOrder(String endpoint, MomoRequest momoRequest) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(momoRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentLength(json.length());
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            System.out.println(entity);
//            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
            HttpEntity<MomoRequest> request = new HttpEntity<>(momoRequest, headers);
            MomoResponse response = restTemplate.postForObject(endpoint, request, MomoResponse.class);
            String responseJson = mapper.writeValueAsString(response);
            System.out.println(responseJson);
//            return mapper.readValue(response.getBody(), MomoResponse.class);
            return response;

        }catch (JsonProcessingException e){
            logger.error("Error while parsing data: {}", e.getMessage());
        }
        return null;
    }


    private String hmacSHA256(String data) {
        try {
            String secret_key = env.getProperty("application.security.momo.secret-key");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKey key = new SecretKeySpec(secret_key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(key);
            return new String(Hex.encode(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error while hashing data: {}", e.getMessage());
        }
        return null;
    }

    private MomoRequest momoRequest(OrderDto order){
        List<OrderDetail> orderDetail = orderDetailService.getOrderDetailByOrderId(order.getId());
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        if(orderDetail!=null){
            orderDetail.forEach(od-> orderDetailDtos.add(od.convertToDto()));
        }
        MomoRequest momoRequest = MomoRequest.builder()
                .partnerCode(env.getProperty("application.security.momo.partner-code"))
                .partnerName("TEST")
                .storeName("FILTRO-COFEE")
                .requestType("captureWallet")
                .redirectUrl("http://localhost:4200/invoice")
                .ipnUrl("https://4c952bfc800a4bdb0f21e4cb9e174b76.serveo.net/api/v1/momo/webhook/ipn")
                .requestId(UUID.randomUUID().toString())
                .amount(Long.valueOf(order.getTotal()))
                .lang("vi")
                .orderId(order.getOrderCode())
                .orderInfo("Thanh toán đơn hàng bằng MOMO")
                .extraData("")
                .build();

        String rawSignature = "accessKey=" + env.getProperty("application.security.momo.access-key")
                +"&amount="+momoRequest.getAmount()+"&extraData="+momoRequest.getExtraData()
                +"&ipnUrl="+momoRequest.getIpnUrl()+"&orderId="+momoRequest.getOrderId()
                +"&orderInfo="+momoRequest.getOrderInfo()+"&partnerCode="+momoRequest.getPartnerCode()
                +"&redirectUrl="+momoRequest.getRedirectUrl()+"&requestId="+momoRequest.getRequestId()
                +"&requestType="+momoRequest.getRequestType();
        String signature = hmacSHA256(rawSignature);
        momoRequest.setSignature(signature);

        List<MomoItems> momoItems = new ArrayList<>();
        for(OrderDetailDto od : orderDetailDtos){
            MomoItems momoItem = MomoItems.builder()
                    .id(od.getProductDetail().getId().toString())
                    .name(od.getProductName())
                    .imageUrl(od.getProductImage().getUrl())
                    .price(Long.valueOf(od.getPrice()))
                    .currency("VND")
                    .quantity(od.getQuantity())
                    .totalPrice(Long.valueOf(od.getTotal()))
                    .build();
            momoItems.add(momoItem);
        }
        momoRequest.setItems(momoItems);
        MomoUserInfo momoUserInfo = MomoUserInfo.builder()
                .name(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhone())
                .build();
        momoRequest.setUserInfo(momoUserInfo);

        MomoDeliveryInfo momoDeliveryInfo = MomoDeliveryInfo.builder()
                .deliveryAddress(order.getAddress() + ", " + order.getWard() + ", " + order.getDistrict() + ", " + order.getProvince())
                .deliveryFee(String.valueOf(order.getShippingFee()))
                .quantity(String.valueOf(momoRequest.getItems().size()))
                .build();
        momoRequest.setDeliveryInfo(momoDeliveryInfo);
        return momoRequest;
    }

}
