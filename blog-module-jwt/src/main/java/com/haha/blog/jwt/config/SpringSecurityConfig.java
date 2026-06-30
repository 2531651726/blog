package com.haha.blog.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.haha.blog.jwt.filter.JwtLoginFilter;
import com.haha.blog.jwt.filter.TokenAuthenticationFilter;
import com.haha.blog.jwt.handler.RestAccessDeniedHandler;
import com.haha.blog.jwt.handler.RestAuthenticationEntryPoint;
import com.haha.blog.jwt.handler.RestAuthenticationFailureHandler;
import com.haha.blog.jwt.handler.RestAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig{

    // 登录成功处理器
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    // 登录失败处理器
    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;
    // 登录校验服务
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    // 未登录处理器
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    // 权限不足处理器
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    // JWT 过滤器
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器，给登录过滤器提供校验能力
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 认证提供者：绑定UserDetailsService、PasswordEncoder，实现数据库账号密码校验
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);   // 绑定登录校验服务
        provider.setPasswordEncoder(passwordEncoder());  // 绑定密码加密器
        return provider;
    }

    /**
     * 核心安全过滤链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf().disable()
                .formLogin().disable()   // 禁用表单登录
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 不创建session
                .authorizeHttpRequests(authorize -> authorize
                        .mvcMatchers("/admin/**").authenticated()
                        .anyRequest().permitAll()
                )
                // 配置全局异常处理器
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 未登录
                .accessDeniedHandler(restAccessDeniedHandler); // 权限不足;
        // 创建登录过滤器
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(objectMapper);
        // 注入认证管理器、登录成功/失败处理器
        jwtLoginFilter.setAuthenticationManager(authenticationManager);
        jwtLoginFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        // 注册自定义认证提供者
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
