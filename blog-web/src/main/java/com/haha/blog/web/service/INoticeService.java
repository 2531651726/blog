package com.haha.blog.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.NoticeDO;
import com.haha.blog.web.domain.vo.notice.NoticeVO;

public interface INoticeService extends IService<NoticeDO> {
    NoticeVO queryNoticeInfo();
}
