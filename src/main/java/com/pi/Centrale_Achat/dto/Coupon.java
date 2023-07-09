package com.pi.Centrale_Achat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Coupon {
    private String code;
    private float discountPercentage;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
}
