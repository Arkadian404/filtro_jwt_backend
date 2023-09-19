package com.ark.security.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {

    private String recipient;
    private String subject;
    private String body;

}
