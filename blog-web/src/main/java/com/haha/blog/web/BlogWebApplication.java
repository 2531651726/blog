package com.haha.blog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootApplication(scanBasePackages = {"com.haha.blog"})
public class BlogWebApplication {



    public static void main(String[] args) {
        String rawPassword = "123456";
        // 加密
        String encryptPwd = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("加密后的密码：" + encryptPwd);
        SpringApplication.run(BlogWebApplication.class, args);
    }

}
