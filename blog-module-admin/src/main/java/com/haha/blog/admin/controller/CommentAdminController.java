package com.haha.blog.admin.controller;

import com.haha.blog.admin.domain.dto.comment.DeleteCommentDTO;
import com.haha.blog.admin.domain.dto.comment.ExamineCommentDTO;
import com.haha.blog.admin.domain.query.comment.CommentPageListQuery;
import com.haha.blog.admin.domain.vo.comment.CommentPageListVO;
import com.haha.blog.admin.service.ICommentAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.utils.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
@Api(tags = "admin 评论模块")
public class CommentAdminController {

    private final ICommentAdminService commentService;

    @PostMapping("/list")
    @ApiOperation(value = "查询评论分页数据")
    @ApiOperationLog(description = "查询评论分页数据")
    public PageDTO<CommentPageListVO> queryCommentPageList(@RequestBody @Validated CommentPageListQuery query) {
        return commentService.queryCommentPageList(query);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "评论删除")
    @ApiOperationLog(description = "评论删除")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteComment(@RequestBody @Validated DeleteCommentDTO dto) {
        commentService.deleteComment(dto);
    }

    @PostMapping("/examine")
    @ApiOperation(value = "评论审核")
    @ApiOperationLog(description = "评论审核")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void examine(@RequestBody @Validated ExamineCommentDTO dto) {
        commentService.examine(dto);
    }
}
