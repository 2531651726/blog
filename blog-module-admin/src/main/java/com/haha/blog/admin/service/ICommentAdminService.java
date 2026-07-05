package com.haha.blog.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.admin.domain.dto.comment.DeleteCommentDTO;
import com.haha.blog.admin.domain.dto.comment.ExamineCommentDTO;
import com.haha.blog.admin.domain.query.comment.CommentPageListQuery;
import com.haha.blog.admin.domain.vo.comment.CommentPageListVO;
import com.haha.blog.common.domain.dos.CommentDO;
import com.haha.blog.common.utils.PageDTO;

public interface ICommentAdminService extends IService<CommentDO> {
    PageDTO<CommentPageListVO> queryCommentPageList(CommentPageListQuery query);

    void deleteComment(DeleteCommentDTO dto);

    void examine(ExamineCommentDTO dto);
}
