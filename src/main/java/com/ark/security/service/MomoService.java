package com.ark.security.service;

import com.ark.security.dto.OrderDetailDto;
import com.ark.security.dto.OrderDto;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.payment.momo.*;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.service.product.ProductDetailService;
import com.ark.security.service.product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
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
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {
    private final String MOMO_API = "https://test-payment.momo.vn/v2/gateway/api";
    private final String RETURN_URL = "https://filtrocoffee.com/payment/momo";
    private final String IPN_API = "https://filtrocoffee.com/springboot";
    private final Environment env;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final OrderService orderService;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(MomoService.class);


    public MomoResponse createMomoOrder(OrderDto orderDto){
        String endpoint = MOMO_API + "/create";
        MomoRequest momoRequest = momoRequest(orderDto);
        return processMomoOrder(endpoint, momoRequest);
    }


    private MomoResponse processMomoOrder(String endpoint, MomoRequest momoRequest) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(momoRequest);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentLength(json.length());
            HttpEntity<MomoRequest> request = new HttpEntity<>(momoRequest, headers);
            return restTemplate.postForObject(endpoint, request, MomoResponse.class);

        }catch (JsonProcessingException e){
            logger.error("Error while parsing data: {}", e.getMessage());
        }
        return null;
    }


    public void processIPN(String request){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            MomoIPN momoIPN = objectMapper.readValue(request, MomoIPN.class);
            momoIPN.setAccessKey(env.getProperty("application.security.momo.access-key"));
            String signature = momoIPN.getSignature();
            String rawSignature = ipnHashedSignature(momoIPN);
            System.out.println(request);
            if(signature.equals(rawSignature) && (verifyOrder(momoIPN) && verifyAmount(momoIPN))){
                    updateOrderStatus(momoIPN);
                    logger.info("IPN verified and updated order status {}, {}", momoIPN.getOrderId(), momoIPN.getResultCode());
            }
        }catch (JsonProcessingException e){
            logger.error("Error: {}", e.getMessage());
        }
    }


    private boolean verifyOrder(MomoIPN momoIPN){
        Order order = orderService.getOrderByOrderCode(momoIPN.getOrderId());
        return order!=null;
    }

    private boolean verifyAmount(MomoIPN momoIPN){
        Order order = orderService.getOrderByOrderCode(momoIPN.getOrderId());
        return String.valueOf(order.getTotal()).equals(String.valueOf(momoIPN.getAmount()));
    }

    private void updateOrderStatus(MomoIPN momoIPN){
        Order order = orderService.getOrderByOrderCode(momoIPN.getOrderId());
        Cart cart = cartService.getCartByUsername(order.getUser().getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        switch (momoIPN.getResultCode().toString()) {
            case "0":
                order.setStatus(OrderStatus.PAID_MOMO);
                break;
            case "1006":
                orderService.rollbackProductStockAndSold(cartItems);
                cart.setStatus(false);
                cartService.saveCart(cart);
                order.setStatus(OrderStatus.CANCELED);
                break;
            default:
                orderService.rollbackProductStockAndSold(cartItems);
                cart.setStatus(false);
                cartService.saveCart(cart);
                order.setStatus(OrderStatus.FAILED);
                break;
        }
        orderService.saveOrder(order);
    }


    private String ipnHashedSignature(MomoIPN momoIPN){
        Field[] fields = momoIPN.getClass().getDeclaredFields();
        List<String> fieldsName = Arrays.stream(fields).map(Field::getName).sorted().toList();
        StringBuilder rawSignature = new StringBuilder();
        for(String fieldName : fieldsName){
            try{
                Field field = momoIPN.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                if(fieldName.equals("signature")){
                    continue;
                }
                String fieldValue = String.valueOf(field.get(momoIPN));
                if(fieldValue!=null){
                    rawSignature.append(fieldName).append("=").append(fieldValue).append("&");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.info("Error: {}", e.getMessage());
            }
        }
        rawSignature.deleteCharAt(rawSignature.length()-1);
        return hmacSHA256(rawSignature.toString());
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
                .redirectUrl(RETURN_URL)
                .ipnUrl(IPN_API + "/api/v1/momo/webhook/ipn")
                .requestId(UUID.randomUUID().toString())
                .amount(Long.valueOf(order.getTotal()))
                .lang("vi")
                .orderId(order.getOrderCode())
                .orderInfo("Thanh toán đơn hàng bằng MOMO")
                .extraData(Base64.encodeBase64String(order.getFullName().getBytes()))
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
