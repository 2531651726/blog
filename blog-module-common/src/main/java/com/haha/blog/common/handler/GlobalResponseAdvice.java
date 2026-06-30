package com.haha.blog.common.handler;

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
        // 如果已经是 Response 类型，则不需要再次包装
        return !Response.class.isAssignableFrom(returnType.getParameterType());
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
