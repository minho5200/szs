package com.szs.prj.controller;

import com.szs.prj.advice.exception.CInvalidException;
import com.szs.prj.compo.WebClientCompo;
import com.szs.prj.dto.*;
import com.szs.prj.entity.User;
import com.szs.prj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/szs")
public class SzsController {

    private final UserService userService;

    private final WebClientCompo webClientCompo;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           return ResponseEntity.badRequest().body("입력된 데이터의 형식이 올바르지 않습니다.");
        }
        dto.setEncRegNo();

        return ResponseEntity.ok(userService.signUp(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/scrap")
    public ResponseEntity<?> scrap(HttpServletRequest req) {
        User user = (User) req.getAttribute("userInfo");

        if (user == null) {
            throw new CInvalidException();
        }

        ScrapReqDto scrapReqDto = new ScrapReqDto();
        scrapReqDto.setName(user.getName());
        scrapReqDto.setRegNo(user.getRegNo());

        //scarp 호출
        ApiResDto scrapData = webClientCompo.scrapWebClient().post()
                .bodyValue(scrapReqDto).retrieve().bodyToMono(ApiResDto.class).block();

        if (scrapData.getErrors().getCode() != null && scrapData.getErrors().getCode().equals("-1")) {
            throw new CInvalidException();
        }

        return ResponseEntity.ok(userService.saveUserTaxaionInfo(scrapData.getData(), user.getUserId()));
    }

    @GetMapping("/refund")
    public ResponseEntity<?> refund(HttpServletRequest req) {
        User user = (User) req.getAttribute("userInfo");
        RefundResDto result = new RefundResDto();
        result.setDecisionTaxAmount(userService.createDecisionTaxAmount(user));
        return ResponseEntity.ok(result);
    }
}
