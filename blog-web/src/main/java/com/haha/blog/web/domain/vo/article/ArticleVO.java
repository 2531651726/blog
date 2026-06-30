package com.haha.blog.web.domain.vo.article;

import com.haha.blog.web.domain.vo.category.CategoryVO;
import com.haha.blog.web.domain.vo.tag.TagVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class ArticleVO {
    private Long id;
    private String cover;
    private String title;
    private LocalDate createDate;
    private String summary;
    /**
     * 文章分类
     */
    private CategoryVO category;

    /**
     * 文章标签
     */
    private List<TagVO> tags;

    private Boolean isTop;
}
