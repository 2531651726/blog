package com.haha.blog.web.service;

import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;

/**
 * <p>
 * 博客设置表 服务类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
public interface IBlogSettingsService extends IService<BlogSettingsDO> {

    BlogSettingsVO queryBlogSettings();
}
