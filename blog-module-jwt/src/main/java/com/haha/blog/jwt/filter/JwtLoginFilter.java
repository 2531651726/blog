package com.haha.blog.jwt.filter;


import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.spring.plugins.secondary.SecondaryVerificationApplication;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haha.blog.jwt.exception.CaptchaVerificationFailedException;
import com.haha.blog.jwt.exception.UsernameOrPasswordNullException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper;
    // 验证码校验器
    @Setter
    private ImageCaptchaApplication imageCaptchaApplication;

    public JwtLoginFilter(ObjectMapper mapper) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 解析request中的数据
        JsonNode jsonNode = mapper.readTree(request.getReader());
        JsonNode usernameNode = jsonNode.get("username");
        JsonNode passwordNode = jsonNode.get("password");
        JsonNode captchaIdNode = jsonNode.get("captchaId");
        // 判断用户名和密码是否为空
        if(Objects.isNull(usernameNode) || Objects.isNull(passwordNode)|| StringUtils.isBlank(usernameNode.textValue()) || StringUtils.isBlank(passwordNode.textValue())) {
            throw new UsernameOrPasswordNullException("用户名或密码不能为空");
        }
        // 二次校验验证码
        if (Objects.nonNull(imageCaptchaApplication)) {
            // 验证码 ID
            String captchaId = captchaIdNode != null ? captchaIdNode.textValue() : null;
            if (StringUtils.isBlank(captchaId)) {
                throw new CaptchaVerificationFailedException("验证码 ID 不能为空");
            }
            // 执行二次校验
            boolean verified = false;
            if (imageCaptchaApplication instanceof SecondaryVerificationApplication) {
                verified = ((SecondaryVerificationApplication) imageCaptchaApplication).secondaryVerification(captchaId);
            }
            if (!verified) {
                throw new CaptchaVerificationFailedException("验证码校验失败，请重新验证");
            }
        }
        String username = usernameNode.textValue();
        String password = passwordNode.textValue();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager ().authenticate (authRequest);
    }
}
