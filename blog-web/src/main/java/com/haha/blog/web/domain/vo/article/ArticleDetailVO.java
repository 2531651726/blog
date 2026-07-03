package com.haha.blog.web.domain.vo.article;

import com.haha.blog.web.domain.vo.tag.TagVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class ArticleDetailVO {
    /**
     * 文章标题
     */
    private String title;
    /**
     * 发布时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 阅读量
     */
    private Long readNum;


    /**
     * 文章正文（HTML）
     */
    private String content;


    /**
     * 分类 ID
     */
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;


    /**
     * 标签集合
     */
    private List<TagVO> tags;


    /**
     * 上一篇文章
     */
    private PreOrNextArticleVO preArticle;
    /**
     * 下一篇文章
     */
    private PreOrNextArticleVO nextArticle;

    /**
     * 总字数
     */
    private Integer totalWords;
    /**
     * 阅读时长
     */
    private String readTime;
}
