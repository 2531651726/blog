package com.haha.blog.web.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.common.domain.dos.NoticeDO;
import com.haha.blog.common.enums.NoticeStatus;
import com.haha.blog.common.mapper.NoticeMapper;
import com.haha.blog.web.domain.vo.notice.NoticeVO;
import com.haha.blog.web.service.INoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeDO> implements INoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public NoticeVO queryNoticeInfo() {
        NoticeDO notice = lambdaQuery()
                .eq(NoticeDO::getStatus, NoticeStatus.SHOW)
                .one();
        NoticeVO noticeVO = new NoticeVO();
        if (notice != null) {
            noticeVO.setContent(notice.getContent());
        }
        return noticeVO;
    }
}
