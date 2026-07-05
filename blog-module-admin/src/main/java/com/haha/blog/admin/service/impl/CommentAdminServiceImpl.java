package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.domain.dto.comment.DeleteCommentDTO;
import com.haha.blog.admin.domain.dto.comment.ExamineCommentDTO;
import com.haha.blog.admin.domain.query.comment.CommentPageListQuery;
import com.haha.blog.admin.domain.vo.comment.CommentPageListVO;
import com.haha.blog.admin.service.ICommentAdminService;
import com.haha.blog.common.domain.dos.CommentDO;
import com.haha.blog.common.enums.CommentStatus;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.CommentMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentAdminServiceImpl extends ServiceImpl<CommentMapper, CommentDO> implements ICommentAdminService {

    private final CommentMapper commentMapper;

    @Override
    public PageDTO<CommentPageListVO> queryCommentPageList(CommentPageListQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String routerUrl = query.getRouterUrl();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        CommentStatus status = query.getStatus();
        // 分页查询
        Page<CommentDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(routerUrl), CommentDO::getRouterUrl, routerUrl)
                .ge(Objects.nonNull(startDateTime), CommentDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), CommentDO::getCreateTime, endDateTime)
                .eq(Objects.nonNull(status), CommentDO::getStatus, status) // 评论状态
                .orderByDesc(CommentDO::getCreateTime)
                .page(new Page<>(current, size));
        List<CommentDO> records = page.getRecords();
        // DO 转 VO
        List<CommentPageListVO> vos = null;
        if (!CollectionUtils.isEmpty(records)) {
            vos = records.stream()
                    .map(commentDO -> BeanUtils.copyBean(commentDO,CommentPageListVO.class))
                    .collect(Collectors.toList());
        }
        return PageDTO.success(page,vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(DeleteCommentDTO dto) {
        Long id = dto.getId();
        CommentDO commentDO = commentMapper.selectById(id);
        if (Objects.isNull(commentDO)) {
            throw new BizException("该评论不存在");
        }
        List<Long> ids =new ArrayList<>();
        ids.add(id);
        // 递归查询所有子评论id
        recursiveCollectChildIds(id, ids);
        commentMapper.deleteBatchIds(ids);
    }

    @Override
    public void examine(ExamineCommentDTO dto) {
        Long id = dto.getId();
        CommentStatus status = dto.getStatus();
        String reason = dto.getReason();
        // 根据 ID 查询评论
        CommentDO commentDO = commentMapper.selectById(id);
        if (Objects.isNull(commentDO)) {
            throw new BizException("该评论不存在");
        }
        CommentStatus currentStatus = commentDO.getStatus();
        if (!Objects.equals(currentStatus, CommentStatus.PENDING_AUDIT)) {
            throw new BizException("该评论未处于待审核状态");
        }
        CommentDO updateDO = new CommentDO()
                .setId(id)
                .setStatus(status)
                .setReason(reason);
        commentMapper.updateById(updateDO);
    }

    private void recursiveCollectChildIds(Long parentId, List<Long> ids) {
        // 1. 查询当前父节点的一级子评论id
        List<Long> childIdList = commentMapper.selectIdsByReplyCommentId(parentId);
        if (CollectionUtils.isEmpty(childIdList)) {
            // 没有子节点，递归终止
            return;
        }
        // 2. 遍历每一个子id
        for (Long childId : childIdList) {
            // 先把当前子id加入总集合
            ids.add(childId);
            // 递归查询这个子评论的后代
            recursiveCollectChildIds(childId, ids);
        }
    }
}
