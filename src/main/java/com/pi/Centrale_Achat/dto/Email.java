package com.pi.Centrale_Achat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Email {
    private String to;
    private String subjuct;
    private String message;

}
