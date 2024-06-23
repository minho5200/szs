package com.szs.prj.dto;

import com.szs.prj.advice.exception.CDuplicatedException;
import com.szs.prj.util.AESUtil;
import lombok.*;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapReqDto {
    private String name;
    private String regNo;

    // 값을 바로 복호화
    public void setRegNo(String regNo){
        try{
            this.regNo = AESUtil.decrypt(regNo);
        } catch (Exception e){
            throw new CDuplicatedException();
        }
    }
}
