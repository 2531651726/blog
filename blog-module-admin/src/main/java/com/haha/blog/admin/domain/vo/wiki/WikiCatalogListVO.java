package com.haha.blog.admin.domain.vo.wiki;

import com.haha.blog.common.enums.WikiCatalogLevel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WikiCatalogListVO {

    private Long id;
    private Long articleId;
    private String title;
    private Integer sort;
    private WikiCatalogLevel level;

    /**
     * 是否处于编辑状态（用于前端是否显示编辑输入框）
     */
    private Boolean editing;

    /**
     * 二级目录
     */
    private List<WikiCatalogListVO> children;
}
