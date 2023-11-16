package com.ark.security.models.payment.momo;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MomoRequest {
    private String partnerCode;
    private String partnerName;
    private String storeName;
    private String requestType;
    private String ipnUrl;
    private String redirectUrl;
    private String requestId;
    private Long amount;
    private String lang;
    private String orderId;
    private String orderInfo;
    private String signature;
    private String extraData;

    private List<MomoItems> items;
    private MomoUserInfo userInfo;
    private MomoDeliveryInfo deliveryInfo;


}
