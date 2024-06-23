package com.szs.prj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiResDto {
    private String status;
    private Errors errors;
    private UserTaxationInfoDto data;

    @lombok.Data
    public static class Errors {
        private String code;
        private String message;
        private String validations;
    }
}
