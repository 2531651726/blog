package com.haha.blog.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "lucene")
@Component
public class LuceneProperties {
    /**
     * 索引存放的文件夹
     */
    private String indexDir;
}
