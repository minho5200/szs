package com.szs.prj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.szs.prj.util.CNumberDeserializer;
import lombok.*;

import java.util.List;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDeductionsDto {

    @JsonProperty("국민연금")
    private List<PensionDto> pensions;

    @JsonProperty("신용카드소득공제")
    private CreditCardIncomDeductionDto creditCardIncomeDeduction;

    // 들어오는 데이터의 형태가 다양하므로 ###,###.00 형식으로 저장
    @JsonProperty("세액공제")
    @JsonDeserialize(using= CNumberDeserializer.class)
    private String taxDeduction;
}
