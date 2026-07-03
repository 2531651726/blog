package com.haha.blog.web.domain.vo.wiki;


import lombok.Data;

import java.util.List;

@Data
public class WikiCatalogVO {

    private Long id;
    private Long articleId;
    private String title;
    private Integer level;

    /**
     * 二级目录
     */
    private List<WikiCatalogVO> children;
}
