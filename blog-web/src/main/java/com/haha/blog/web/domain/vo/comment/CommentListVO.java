package com.haha.blog.web.domain.vo.comment;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CommentListVO {
    /**
     * 总评论数
     */
    private Integer total;

    /**
     * 评论集合
     */
    private List<CommentVO> comments;
}
