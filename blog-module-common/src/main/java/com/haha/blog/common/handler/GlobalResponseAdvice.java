package com.haha.blog.common.handler;

import com.haha.blog.common.aspect.SkipResponseWrap;
import com.haha.blog.common.utils.JsonUtil;
import com.haha.blog.common.utils.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {
        "com.haha.blog.web.controller",
        "com.haha.blog.admin.controller"
})
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 1. 方法加了@SkipResponseWrap注解 直接跳过
        // 2. 返回值本身就是Response类型 也跳过
        boolean skipByAnnotation = returnType.hasMethodAnnotation(SkipResponseWrap.class);
        boolean skipByReturnType = Response.class.isAssignableFrom(returnType.getParameterType());
        // 两个条件任意一个满足 → 不执行全局包装
        return !skipByAnnotation && !skipByReturnType;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Response) {
            return body;
        }
        // 特殊处理 String 类型：
        // 如果 Controller 返回 String，Spring 默认使用 StringHttpMessageConverter。
        // 若直接返回 Response 对象，会产生 ClassCastException。
        if (body instanceof String) {
            return JsonUtil.toJsonString(Response.success(body));
        }
        // 其他类型直接包装成 Response.success()
        return Response.success(body);
    }
}
