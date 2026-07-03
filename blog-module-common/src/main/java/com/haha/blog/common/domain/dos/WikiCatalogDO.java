package com.haha.blog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.haha.blog.common.enums.WikiCatalogLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 知识库目录表
 * </p>
 *
 * @author li
 * @since 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wiki_catalog")
public class WikiCatalogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 知识库id
     */
    private Long wikiId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 目录层级
     */
    private WikiCatalogLevel level;

    /**
     * 父目录id
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

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
    private Integer isDeleted;


}
