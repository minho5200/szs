package com.szs.prj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTaxationInfoDto {

    @JsonProperty("이름")
    private String name;

    @JsonProperty("종합소득금액")
    private long totalIncome;

    @JsonProperty("소득공제")
    private IncomeDeductionsDto incomeDeductions;

}
