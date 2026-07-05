package com.haha.blog.web.domain.vo.comment;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class CommentVO {

    private Long id;
    private String content;
    private String avatar;
    private String nickname;
    private String website;
    private LocalDateTime createTime;

    /**
     * 回复用户的昵称
     */
    private String replyNickname;
    /**
     * 子评论集合
     */
    private List<CommentVO> childComments;
    /**
     * 是否展示回复表单（默认 false）
     */
    private Boolean isShowReplyForm;

}
