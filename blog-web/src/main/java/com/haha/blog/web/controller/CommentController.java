package com.haha.blog.web.controller;

import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.web.domain.dto.comment.PublishCommentDTO;
import com.haha.blog.web.domain.query.comment.CommentListQuery;
import com.haha.blog.web.domain.query.comment.QQUserInfoQuery;
import com.haha.blog.web.domain.vo.comment.CommentListVO;
import com.haha.blog.web.domain.vo.comment.QQUserInfoVO;
import com.haha.blog.web.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论模块")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @PostMapping("/qq/userInfo")
    @ApiOperation("获取 QQ 用户信息")
    @ApiOperationLog(description = "获取 QQ 用户信息")
    public QQUserInfoVO queryQQUserInfo(@RequestBody @Validated QQUserInfoQuery query) {
        return commentService.queryQQUserInfo(query);
    }

    @PostMapping("/publish")
    @ApiOperation(value = "发布评论")
    @ApiOperationLog(description = "发布评论")
    public void publishComment(@RequestBody @Validated PublishCommentDTO dto) {
        commentService.publishComment(dto);
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取页面所有评论")
    @ApiOperationLog(description = "获取页面所有评论")
    public CommentListVO queryCommentList(@RequestBody @Validated CommentListQuery query) {
        return commentService.queryCommentList(query);
    }

}
