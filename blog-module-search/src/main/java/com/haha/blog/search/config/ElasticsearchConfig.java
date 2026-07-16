package com.haha.blog.search.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String esUri;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        log.info("正在初始化ES客户端，连接地址：{}", esUri);
        // 剔除协议头
        String addr = esUri.replace("http://", "").replace("https://", "");
        // 只分割成前后两段，避免异常
        String[] hostPort = addr.split(":", 2);
        String host = hostPort[0];
        int port = 9200;
        if (hostPort.length == 2) {
            try {
                port = Integer.parseInt(hostPort[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ES端口配置格式不正确：" + esUri, e);
            }
        }

        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port, "http"));
        // 设置常用超时时间，防止请求卡死
        builder.setRequestConfigCallback(config -> config
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(5000));

        return new RestHighLevelClient(builder);
    }

}
