package com.szs.prj;

import com.szs.prj.compo.JwtCompo;
import com.szs.prj.dto.LoginReqDto;
import com.szs.prj.dto.RefundResDto;
import com.szs.prj.dto.SignUpReqDto;
import com.szs.prj.entity.User;
import com.szs.prj.repository.UserRepository;
import com.szs.prj.service.UserService;
import com.szs.prj.util.AESUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    //회원가입 테스트
    @Test
    void signUpTest() throws Exception {
        // Given
        SignUpReqDto dto = new SignUpReqDto("test5","test1234","조조","810326-2715702");

        String jsonDto = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isOk());

        User userBefore = User.builder().userId("test5").password(AESUtil.encrypt("test1234"))
                .regNo(AESUtil.encrypt("810326-2715702")).name("조조").build();
        User userAfter = userRepository.findById(dto.getUserId()).orElseThrow();

        assertEquals(userBefore.getName(), userAfter.getName());
    }

    @Autowired
    JwtCompo jwtCompo;

    // 로그인 테스트
    @Test
    void loginTest() throws Exception {
        signUpTest();

        LoginReqDto dto = new LoginReqDto("test5", "test1234");
        String jsonDto = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(jwtCompo.createToken(dto.getUserId())));
    }


    //스크랩 테스트
    @Test
    void scrapTest() throws Exception {
        signUpTest();

        String token = jwtCompo.createToken("test5");

        mockMvc.perform(post("/szs/scrap")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(""))
                .andExpect(status().isOk());
    }

    @Autowired
    UserService userService;

    @Test
    void refundTest() throws Exception {
        scrapTest();
        String token = jwtCompo.createToken("test5");

        mockMvc.perform(get("/szs/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(""))
                .andExpect(status().isOk());

        User user = userRepository.findById("test5").get();
        RefundResDto resultBefore = new RefundResDto();

        resultBefore.setDecisionTaxAmount(userService.createDecisionTaxAmount(user));
        RefundResDto resultAfter = new RefundResDto("7,749,096");

        assertEquals(resultBefore.getDecisionTaxAmount(), resultAfter.getDecisionTaxAmount());
    }
}
