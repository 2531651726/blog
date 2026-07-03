package com.haha.blog.common.domain.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.haha.blog.common.enums.PublishStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 知识库表
 * </p>
 *
 * @author li
 * @since 2026-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_wiki")
public class WikiDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 摘要
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
     * 权重，用于是否置顶（0: 未置顶；>0: 参与置顶，权重值越高越靠前）
     */
    private Integer weight;

    /**
     * 是否发布：0：未发布 1：已发布
     */
    private PublishStatus isPublish;

    /**
     * 删除标志位：0：未删除 1：已删除
     */
    private Integer isDeleted;

}
