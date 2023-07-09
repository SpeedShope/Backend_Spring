package com.pi.Centrale_Achat.dto;

import lombok.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto {
    private float tauxDiscount;
    private Date startDateDiscount;
    private Date endDateDiscount;
}