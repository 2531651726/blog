package com.haha.blog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.haha.blog.common.enums.ArticleType;
import com.haha.blog.common.enums.PublishStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author li
 * @since 2026-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_article")
public class ArticleDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 文章摘要
     */
    private String summary;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志位：0：未删除 1：已删除
     */
    private Boolean isDeleted;

    /**
     * 被阅读次数
     */
    private Long readNum;
    /**
     * 权重，决定文章展示顺序
     */
    private Integer weight;
    /**
     * 文章类型
     */
    private ArticleType type;
    /**
     * 是否发布：0：未发布 1：已发布
     */
    private PublishStatus isPublish;

}
