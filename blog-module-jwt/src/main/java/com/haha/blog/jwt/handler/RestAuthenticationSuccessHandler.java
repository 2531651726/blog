package com.haha.blog.jwt.handler;

import com.haha.blog.common.utils.Response;
import com.haha.blog.jwt.domain.LoginRspVO;
import com.haha.blog.jwt.utils.JwtTokenHelper;
import com.haha.blog.jwt.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenHelper jwtTokenHelper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails)authentication.getPrincipal();
        String username = principal.getUsername();
        String token = jwtTokenHelper.generateToken(username);
        // 返回token
        LoginRspVO vo = LoginRspVO.builder().token(token).build();
        ResultUtil.ok(response, Response.success(vo));
    }
}
