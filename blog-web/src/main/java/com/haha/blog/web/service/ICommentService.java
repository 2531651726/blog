package com.haha.blog.web.service;

import com.haha.blog.web.domain.dto.comment.PublishCommentDTO;
import com.haha.blog.web.domain.query.comment.CommentListQuery;
import com.haha.blog.web.domain.query.comment.QQUserInfoQuery;
import com.haha.blog.web.domain.vo.comment.CommentListVO;
import com.haha.blog.web.domain.vo.comment.QQUserInfoVO;

public interface ICommentService {
    QQUserInfoVO queryQQUserInfo(QQUserInfoQuery query);

    void publishComment(PublishCommentDTO dto);

    CommentListVO queryCommentList(CommentListQuery query);
}
