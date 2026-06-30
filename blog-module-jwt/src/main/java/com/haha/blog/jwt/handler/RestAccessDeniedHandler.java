package com.haha.blog.jwt.handler;

import com.haha.blog.common.enums.ResponseCodeEnum;
import com.haha.blog.common.utils.Response;
import com.haha.blog.jwt.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("登录成功访问受保护的资源，但是权限不够: ", accessDeniedException);
        ResultUtil.fail(response, HttpStatus.FORBIDDEN.value(), Response.fail(ResponseCodeEnum.FORBIDDEN));
    }
}
