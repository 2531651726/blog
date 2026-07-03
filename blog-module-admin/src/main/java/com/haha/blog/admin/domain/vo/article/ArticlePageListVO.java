package com.haha.blog.admin.domain.vo.article;

import com.haha.blog.common.enums.PublishStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePageListVO {
    /**
     * 文章 ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否发布：0：未发布 1：已发布
     */
    private PublishStatus isPublish;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;
}
