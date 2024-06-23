package com.szs.prj.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreditCardIncomDeductionDto {
    private int year;
    private List<Map<String, String>> month; // 맵은 월, 금액
}
