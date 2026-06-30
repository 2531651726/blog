package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.haha.blog.admin.domain.dto.BlogSettings.UpdateBlogSettingsDTO;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;

public interface IBlogSettingsAdminService extends IService<BlogSettingsDO> {
    void updateBlogSettings(UpdateBlogSettingsDTO dto);

    BlogSettingsVO findDetail();
}
