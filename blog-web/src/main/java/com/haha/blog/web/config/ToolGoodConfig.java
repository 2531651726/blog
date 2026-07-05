package com.haha.blog.web.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import toolgood.words.IllegalWordsSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class ToolGoodConfig {

    @Bean
    @SuppressWarnings("deprecation")
    public IllegalWordsSearch illegalWordsSearch(ResourceLoader resourceLoader) throws IOException {
        IllegalWordsSearch illegalWordsSearch = new IllegalWordsSearch();
        // 读取 /resource 目录下的敏感词 txt 文件
        List<String> sensitiveWords = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceLoader.getResource("classpath:word/sensi_words.txt").getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(line.trim())) {
                    sensitiveWords.add(line.trim());
                }
            }
        }
        illegalWordsSearch.SetKeywords(sensitiveWords);   // 设置敏感词
        return illegalWordsSearch;
    }
}
