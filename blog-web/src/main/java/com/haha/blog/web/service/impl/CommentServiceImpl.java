package com.haha.blog.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.haha.blog.common.domain.dos.CommentDO;
import com.haha.blog.common.enums.CommentStatus;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.BlogSettingsMapper;
import com.haha.blog.common.mapper.CommentMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.web.domain.dto.comment.PublishCommentDTO;
import com.haha.blog.web.domain.query.comment.CommentListQuery;
import com.haha.blog.web.domain.query.comment.QQUserInfoQuery;
import com.haha.blog.web.domain.vo.comment.CommentVO;
import com.haha.blog.web.domain.vo.comment.CommentListVO;
import com.haha.blog.web.domain.vo.comment.QQUserInfoVO;
import com.haha.blog.web.event.message.CommentEvent;
import com.haha.blog.web.service.ICommentService;
import com.haha.blog.web.utils.QQUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import toolgood.words.IllegalWordsSearch;
import toolgood.words.IllegalWordsSearchResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements ICommentService {

    private final BlogSettingsMapper blogSettingsMapper;
    private final CommentMapper commentMapper;
    private final IllegalWordsSearch wordsSearch;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public QQUserInfoVO queryQQUserInfo(QQUserInfoQuery query) {
        String qq = query.getQq().trim();
        if(!QQUtil.isLegalQQ(qq)){
            log.warn("QQ号格式错误: {}", qq);
            throw new BizException("QQ号格式错误");
        }
        // 获取头像
        String url = String.format("https://q.qlogo.cn/headimg_dl?dst_uin=" + qq + "&spec=100");
        // 封装结果
        QQUserInfoVO vo = new QQUserInfoVO();
        vo.setMail(qq + "@qq.com");
        vo.setAvatar(url);
        vo.setNickname("");
        return vo;
    }

    @Override
    public void publishComment(PublishCommentDTO dto) {
        CommentDO commentDO = BeanUtils.copyBean(dto, CommentDO.class);
        BlogSettingsDO blogSettings = blogSettingsMapper.selectOne(null);
        // 是否开启敏感词过滤
        Boolean isCommentSensiWordOpen = blogSettings.getIsCommentSensiWordOpen();
        // 是否开启审核
        Boolean isCommentExamineOpen = blogSettings.getIsCommentExamineOpen();
        CommentStatus status = CommentStatus.NORMAL;
        if(isCommentExamineOpen){
            status = CommentStatus.PENDING_AUDIT;
        }
        // 判断是否包含敏感词
        boolean isContainSensitiveWord = false;
        if(isCommentSensiWordOpen){
            // 校验是否开启的敏感词过滤
            isContainSensitiveWord = wordsSearch.ContainsAny(dto.getContent());
            if(isContainSensitiveWord){
                status = CommentStatus.AUDIT_REJECT;
                // 匹配到的所有敏感词组
                List<IllegalWordsSearchResult> results = wordsSearch.FindAll(dto.getContent());
                List<String> keywords = results.stream().map(result -> result.Keyword).collect(Collectors.toList());
                // 审核不通过的原因
                String reason = String.format("系统自动拦截，包含敏感词：%s", keywords);
                commentDO.setReason(reason);
            }
        }
        commentDO.setStatus(status);
        commentMapper.insert(commentDO);
        Long commentId = commentDO.getId();
        // 发布事件
        eventPublisher.publishEvent(new CommentEvent(this,commentId));
        if (isContainSensitiveWord){
            throw new BizException("评论内容中包含敏感词，请重新编辑后再提交");
        }

        if (Objects.equals(status, CommentStatus.PENDING_AUDIT)) {
            throw new BizException("评论已提交, 等待博主审核通过");
        }
    }

    @Override
    public CommentListVO queryCommentList(CommentListQuery query) {
        String routerUrl = query.getRouterUrl();
        // 查询该文章下所有状态为“正常”的评论
        List<CommentDO> commentDOS = commentMapper.selectByRouterUrlAndStatus(routerUrl, CommentStatus.NORMAL);
        // 总评论数
        Integer total = commentDOS.size();
        List<CommentVO> vos = null;
        // DO 转 VO
        if(!CollectionUtil.isEmpty(commentDOS)){
            vos = commentDOS.stream()
                    .filter(commentDO -> Objects.isNull(commentDO.getParentCommentId()))
                    .map(commontDO -> {
                        CommentVO commentVO = BeanUtils.copyBean(commontDO, CommentVO.class);
                        commentVO.setIsShowReplyForm(false);
                        return commentVO;
                    })
                    .collect(Collectors.toList());
            vos.forEach(vo -> {
                Long parentCommentId = vo.getId();
                List<CommentVO> childComments = commentDOS.stream()
                        .filter(commentDO -> Objects.equals(commentDO.getParentCommentId(), parentCommentId))
                        .sorted(Comparator.comparing(CommentDO::getCreateTime))
                        .map(commentDO -> {
                            CommentVO commentVO = BeanUtils.copyBean(commentDO, CommentVO.class);
                            Long replyCommentId = commentDO.getReplyCommentId();
                            // 若二级评论的 replayCommentId 不等于一级评论 ID, 前端则需要展示【回复 @ xxx】，需要设置回复昵称
                            if(!Objects.equals(replyCommentId, parentCommentId)){
                                Optional<CommentDO> optionalCommentDO = commentDOS.stream()
                                        .filter(commentDO1 -> Objects.equals(commentDO1.getId(), replyCommentId)).findFirst();
                                optionalCommentDO.ifPresent(DO -> commentVO.setReplyNickname(DO.getNickname()));
                            }
                            return commentVO;
                        })
                        .collect(Collectors.toList());
                vo.setChildComments(childComments);
            });
        }
        return new CommentListVO()
                .setTotal(total)
                .setComments(vos);
    }
}
