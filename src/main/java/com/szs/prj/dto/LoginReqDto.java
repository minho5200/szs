package com.szs.prj.dto;

import com.szs.prj.advice.exception.CDuplicatedException;
import com.szs.prj.util.AESUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @Schema(description = "사용자 아이디", example = "test")
    public String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "사용자 비밀번호", example = "test1234")
    public String password;

}
