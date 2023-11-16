package com.ark.security.models.payment.momo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoUserInfo {
    private String name;
    private String phoneNumber;
    private String email;
}
