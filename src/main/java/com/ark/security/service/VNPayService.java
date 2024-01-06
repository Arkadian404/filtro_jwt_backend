package com.ark.security.service;

import com.ark.security.dto.OrderDto;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.payment.vnpay.VNPIPN;
import com.ark.security.models.payment.vnpay.VNPIPNResponse;
import com.ark.security.models.payment.vnpay.VNPRequest;
import com.ark.security.models.payment.vnpay.VNPResponse;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.service.product.ProductDetailService;
import com.ark.security.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService {
    private final Logger logger = LoggerFactory.getLogger(VNPayService.class);
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final String RETURN_URL = "http://localhost:4200/payment/vnpay";
    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public VNPResponse createVNPayOrder(OrderDto orderDto, HttpServletRequest req){
        String data = vnpRequest(orderDto, req);
        return VNPResponse.builder()
                .status("OK")
                .message("success")
                .paymentUrl(data)
                .build();
    }


    public VNPIPNResponse processIpn(Map<String, String> params){
        try{
            ObjectMapper mapper = new  ObjectMapper();
            VNPIPN vnpipn = mapper.convertValue(params, VNPIPN.class);
            VNPIPNResponse response;
            if(validateSignature(vnpipn)){
                if(verifyOrder(vnpipn)){
                    if(verifyAmount(vnpipn)){
                        if(verifyOrderStatus(vnpipn)){
                            updateOrderStatus(vnpipn);
                            response = VNPIPNResponse.builder()
                                    .RspCode("00")
                                    .Message("Confirm Success")
                                    .build();
                            logger.info("VNPay IPN: {}", vnpipn.getVnp_ResponseCode());
                        }
                        else{
                            response = VNPIPNResponse.builder()
                                    .RspCode("02")
                                    .Message("Order already confirmed")
                                    .build();
                            logger.info("VNPay IPN Order: {}", vnpipn.getVnp_ResponseCode());
                        }
                    }else{
                        response = VNPIPNResponse.builder()
                                .RspCode("04")
                                .Message("Invalid amount")
                                .build();
                        logger.info("VNPay IPN Amount: {}", vnpipn.getVnp_ResponseCode());
                    }
                }else{
                    response = VNPIPNResponse.builder()
                            .RspCode("01")
                            .Message("Invalid order")
                            .build();
                    logger.info("VNPay IPN Check Order: {}", vnpipn.getVnp_ResponseCode());
                }
            }else{
                response = VNPIPNResponse.builder()
                        .RspCode("97")
                        .Message("Invalid signature")
                        .build();
                logger.info("VNPay IPN Signature: {}", vnpipn.getVnp_ResponseCode());
            }
            logger.info("VNPay IPN Response: {}", response);
            return response;
        }catch (Exception e) {
            logger.error("Error when processing IPN: {}", e.getMessage());
        }
        return VNPIPNResponse.builder()
                .RspCode("99")
                .Message("Unknown error")
                .build();
    }

    private boolean validateSignature(VNPIPN vnpipn){
        String vnp_SecureHash = vnpipn.getVnp_SecureHash();
        vnpipn.setVnp_SecureHash(null);
        String rawData = hashAllFields(vnpipn);
        String mySecureHash = hmacSHA512(env.getProperty("application.security.vnpay.vnp_HashSecret"), rawData);
        return vnp_SecureHash.equals(mySecureHash);
    }

    private boolean verifyAmount(VNPIPN vnpipn){
        Order order = orderService.getOrderByOrderCode(vnpipn.getVnp_TxnRef());
        if(order != null){
            return order.getTotal() == Integer.parseInt(vnpipn.getVnp_Amount())/100;
        }
        return false;
    }

    private boolean verifyOrder(VNPIPN vnpipn){
        Order order = orderService.getOrderByOrderCode(vnpipn.getVnp_TxnRef());
        return order != null;
    }

    private boolean verifyOrderStatus(VNPIPN vnpipn){
        Order order = orderService.getOrderByOrderCode(vnpipn.getVnp_TxnRef());
        if(order != null){
            return order.getStatus().equals(OrderStatus.PENDING);
        }
        return false;
    }

    private void updateOrderStatus(VNPIPN vnpipn){
        Order order = orderService.getOrderByOrderCode(vnpipn.getVnp_TxnRef());
        Cart cart = cartService.getCartByUsername(order.getUser().getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        switch (vnpipn.getVnp_ResponseCode()) {
            case "00":
                order.setStatus(OrderStatus.PAID_VNPAY);
                break;
            case "24":
                orderService.rollbackProductStockAndSold(cartItems);
                order.setStatus(OrderStatus.CANCELED);
                break;
            default:
                orderService.rollbackProductStockAndSold(cartItems);
                order.setStatus(OrderStatus.FAILED);
                break;
        }
        orderService.saveOrder(order);
    }

//    private void updateProductStockAndSold(Order order){
//        List<OrderDetail> orderDetails = order.getOrderDetails();
//        for(OrderDetail orderDetail: orderDetails){
//            ProductDetail pd = orderDetail.getProductDetail();
//            pd.setStock(pd.getStock() - orderDetail.getQuantity());
//            productDetailService.saveProductDetail(pd);
//            Product product = orderDetail.getProductDetail().getProduct();
//            product.setSold(product.getSold() + orderDetail.getQuantity());
//            productService.save(product);
//        }
//    }

    private String vnpRequest(OrderDto orderDto, HttpServletRequest req){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = dateFormat.format(calendar.getTime());
        VNPRequest request = VNPRequest.builder()
                .vnp_Version("2.1.0")
                .vnp_Command("pay")
                .vnp_Amount(String.valueOf(orderDto.getTotal()*100))
                .vnp_TxnRef(orderDto.getOrderCode())
                .vnp_BankCode("")
                .vnp_IpAddr(getIpAddress(req))
                .vnp_TmnCode(env.getProperty("application.security.vnpay.vnp_TmnCode"))
                .vnp_CurrCode("VND")
                .vnp_Inv_Company("FILTRO")
                .vnp_OrderType("other")
                .vnp_OrderInfo("Thanh toán bằng VNPay")
                .vnp_Locale("vn")
                .vnp_ReturnUrl(RETURN_URL)
                .vnp_CreateDate(vnp_CreateDate)
                .vnp_ExpireDate(vnp_ExpireDate)
                .build();

        String rawData = hashAllFields(request);
        String secureHash = hmacSHA512(env.getProperty("application.security.vnpay.vnp_HashSecret"), rawData);
        request.setVnp_SecureHash(secureHash);
        return vnp_PayUrl + "?"+ rawData + "&vnp_SecureHash=" + secureHash;
    }


    private  String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }


    private String hmacSHA512(String key, String data){
        try{
            if(key == null  || data== null){
                throw new NullPointerException();
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2*result.length);
            for(byte b: result){
                sb.append(String.format("%02x", b&0xff));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException  | InvalidKeyException e) {
            logger.error("Error when generating HMAC SHA512: {}", e.getMessage());
            return null;
        }
    }


    private String hashAllFields(Object request){
        Field[] fields = request.getClass().getDeclaredFields();
        List<String> fieldNames = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames){
            try{
                Field field = request.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                String fieldValue = (String) field.get(request);
                if(fieldValue != null && !fieldValue.isEmpty()){
                    sb.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                    sb.append("=");
                    sb.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    sb.append("&");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("Error when getting field value: {}", e.getMessage());
            }
        }
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

}
