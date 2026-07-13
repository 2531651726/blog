package com.haha.blog.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /*@Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(7000); // 连接超时时间
        factory.setReadTimeout(7000); // 读取超时时间
        return new RestTemplate(factory);
    }*/

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);           // 连接池最大连接数
        connectionManager.setDefaultMaxPerRoute(30);  // 每个路由最大连接数
        return connectionManager;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(
            PoolingHttpClientConnectionManager connectionManager) {
        // 请求超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(7000)  // 连接超时：7秒
                .setSocketTimeout(7000)   // 读取超时：7秒
                .setConnectionRequestTimeout(5000) // 从连接池获取连接超时：5秒
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

}
