package com.haha.blog.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.domain.dto.notice.AddNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.DeleteNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeStatusDTO;
import com.haha.blog.admin.domain.query.notice.NoticePageListQuery;
import com.haha.blog.admin.domain.vo.notice.NoticePageListVO;
import com.haha.blog.admin.service.INoticeAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.common.domain.dos.ArticleDO;
import com.haha.blog.common.domain.dos.NoticeDO;
import com.haha.blog.common.enums.ArticleType;
import com.haha.blog.common.enums.NoticeStatus;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.NoticeMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.common.utils.PageDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeAdminServiceImpl extends ServiceImpl<NoticeMapper, NoticeDO> implements INoticeAdminService {

    private final NoticeMapper noticeMapper;

    @Override
    public void addNotice(AddNoticeDTO dto) {
        String content = dto.getContent();
        NoticeStatus status = dto.getIsShow();
        NoticeDO noticeDO = new NoticeDO()
                .setContent(content)
                .setStatus(status);
        int row = noticeMapper.insert(noticeDO);
        if (row == 0) {
            throw new BizException("新增公告失败");
        }
    }

    @Override
    public void updateNotice(UpdateNoticeDTO dto) {
        Long id = dto.getId();
        String content = dto.getContent();
        NoticeDO noticeDO = new NoticeDO()
                .setId(id)
                .setContent(content);
        int row = noticeMapper.updateById(noticeDO);
        if (row == 0) {
            throw new BizException("更新公告失败");
        }
    }

    @Override
    public void deleteNotice(DeleteNoticeDTO dto) {
        Long id = dto.getId();
        int row = noticeMapper.deleteById(id);
        if (row == 0) {
            throw new BizException("删除公告失败");
        }
    }

    @Override
    public PageDTO<NoticePageListVO> queryNoticePageList(NoticePageListQuery query) {
        Long current = query.getCurrent();
        Long size = query.getSize();
        // 获取查询条件
        String content = query.getContent();
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        NoticeStatus status = query.getIsShow();
        // 分页查询
        Page<NoticeDO> page = lambdaQuery()
                .like(StringUtils.isNoneBlank(content), NoticeDO::getContent, content)
                .ge(Objects.nonNull(startDateTime), NoticeDO::getCreateTime, startDateTime)
                .le(Objects.nonNull(endDateTime), NoticeDO::getCreateTime, endDateTime)
                .eq(Objects.nonNull(status), NoticeDO::getStatus, status)
                .orderByDesc(NoticeDO::getCreateTime)
                .page(new Page<>(current, size));
        List<NoticeDO> records = page.getRecords();
        if(CollectionUtil.isEmpty(records)){
            return PageDTO.success(page, Collections.emptyList());
        }
        List<NoticePageListVO> list = records.stream()
                .map(noticeDO -> {
                    NoticePageListVO vo = BeanUtils.copyBean(noticeDO, NoticePageListVO.class);
                    vo.setIsShow(noticeDO.getStatus());
                    return vo;
                })
                .collect(Collectors.toList());
        return PageDTO.success(page, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNoticeStatus(UpdateNoticeStatusDTO dto) {
        Long id = dto.getId();
        NoticeStatus status = dto.getIsShow();
        if(Objects.equals(status,NoticeStatus.SHOW)){
            boolean update = lambdaUpdate()
                    .set(NoticeDO::getStatus, NoticeStatus.HIDE)
                    .update();
            if(!update){
                throw new BizException("更新公告状态失败");
            }
        }
        NoticeDO noticeDO = new NoticeDO()
                .setId(id)
                .setStatus(status);
        int row = noticeMapper.updateById(noticeDO);
        if (row == 0) {
            throw new BizException("更新公告状态失败");
        }
    }


}
