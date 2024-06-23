package com.szs.prj.dto;


import com.szs.prj.advice.exception.CDuplicatedException;
import com.szs.prj.advice.exception.CInvalidException;
import com.szs.prj.util.AESUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Schema(description = "사용자 아이디", example = "test")
    public String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(description = "사용자 비밀번호", example = "test1234")
    public String password;

    @NotBlank(message = "이름을 입력해주세요")
    @Schema(description = "사용자 이름", example = "동탁")
    public String name;

    @NotBlank
    @Size(min = 14, max = 14, message = "주민등록번호는 -을 포함하여 14글자를 입력해주세요.")
    @Schema(description = "사용자 주민등록번호(YYMMDD-gabcdef)", example = "921108-1582816")
    @Pattern(regexp = "\\d{6}-[1-4]\\d{6}", message = "올바른 주민등록번호 형식을 입력해주세요. (YYMMDD-gabcdef)")
    public String regNo;


    //해당 요청값이 들어오면 바로 encrypt
    public void setPassword(String password) {
        try{
            this.password = AESUtil.encrypt(password);
        } catch (Exception e){
            throw new CInvalidException();
        }
    }

    public void setEncRegNo() {
        try{
            this.regNo = AESUtil.encrypt(this.regNo);
        } catch (Exception e){
            throw new CInvalidException();
        }
    }

}
