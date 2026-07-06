package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.admin.domain.dto.notice.AddNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.DeleteNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeDTO;
import com.haha.blog.admin.domain.dto.notice.UpdateNoticeStatusDTO;
import com.haha.blog.admin.domain.query.notice.NoticePageListQuery;
import com.haha.blog.admin.domain.vo.notice.NoticePageListVO;
import com.haha.blog.common.domain.dos.NoticeDO;
import com.haha.blog.common.utils.PageDTO;

public interface INoticeAdminService extends IService<NoticeDO> {
    void addNotice(AddNoticeDTO dto);

    void updateNotice(UpdateNoticeDTO dto);

    void deleteNotice(DeleteNoticeDTO dto);

    PageDTO<NoticePageListVO> queryNoticePageList(NoticePageListQuery query);

    void updateNoticeStatus(UpdateNoticeStatusDTO dto);
}
