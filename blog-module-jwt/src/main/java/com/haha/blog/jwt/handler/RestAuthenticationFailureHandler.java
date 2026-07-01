package com.haha.blog.jwt.handler;

import com.haha.blog.common.enums.ResponseCodeEnum;
import com.haha.blog.common.utils.Response;
import com.haha.blog.jwt.exception.CaptchaVerificationFailedException;
import com.haha.blog.jwt.exception.UsernameOrPasswordNullException;
import com.haha.blog.jwt.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("AuthenticationException:",exception);
        if(exception instanceof UsernameOrPasswordNullException){
            // 用户名、密码参数为空
            ResultUtil.fail(response, Response.fail(exception.getMessage()));
            return;
        }else if(exception instanceof BadCredentialsException){
            // 用户名不存在 / 密码错误
            ResultUtil.fail(response, Response.fail(ResponseCodeEnum.USERNAME_OR_PWD_ERROR));
            return;
        }else if (exception instanceof CaptchaVerificationFailedException) {
            // 行为验证码错误
            ResultUtil.fail(response, Response.fail(exception.getMessage()));
        }
        ResultUtil.fail(response, Response.fail(ResponseCodeEnum.LOGIN_FAIL));
    }
}
