package com.szs.prj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PensionDto {
    @JsonProperty("월")
    private String month;

    @JsonProperty("공제액")
    private String deductionAmount;
}
