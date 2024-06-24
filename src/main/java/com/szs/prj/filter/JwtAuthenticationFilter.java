package com.szs.prj.filter;

import com.szs.prj.compo.JwtCompo;
import com.szs.prj.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtCompo jwtCompo;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtCompo.resolveToken(request);

        User user = null;
        // 토큰 확인
        if (token != null && jwtCompo.validateToken(token)) {
            token = token.split(" ")[1].trim();
            try {
                // 외부 api 를 호출 하기 위한 정보 추출
                user = jwtCompo.getUserInfo(token);

                // 사용자 인증 설정
                AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, new ArrayList<>());
                authenticated.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticated);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // UserTaxationInfoDto 객체를 HttpServletRequest에 설정
        request.setAttribute("userInfo", user);

        filterChain.doFilter(request, response);
    }
}