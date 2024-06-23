package com.szs.prj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.text.DecimalFormat;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundResDto {

    @JsonProperty("결정세액")
    private String decisionTaxAmount;


    // 천단위 구분자를 바로 넣어준다.
    public void setDecisionTaxAmount(long decisionTaxAmount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        this.decisionTaxAmount = formatter.format(decisionTaxAmount);
    }
}
