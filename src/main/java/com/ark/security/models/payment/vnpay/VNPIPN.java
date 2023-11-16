package com.ark.security.models.payment.vnpay;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPIPN {
    private String vnp_TmnCode;
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_TransactionNo;
    private String vnp_ResponseCode;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_PayDate;
    private String vnp_SecureHash;
}
