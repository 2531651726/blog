package com.haha.blog.web.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.impl.LocalMemoryResourceStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaResourceConfig {

    @Bean
    public ResourceStore resourceStore() {
        LocalMemoryResourceStore resourceStore = new LocalMemoryResourceStore();
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/a.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/b.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/c.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/d.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "bgimages/e.jpg", "default"));
        return resourceStore;
    }
}
