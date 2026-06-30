package com.haha.blog.web.service.impl;

import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.BlogSettingsMapper;
import com.haha.blog.common.utils.BeanUtils;
import com.haha.blog.web.service.IBlogSettingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 博客设置表 服务实现类
 * </p>
 *
 * @author li
 * @since 2026-06-21
 */
@Service
@RequiredArgsConstructor
public class BlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements IBlogSettingsService {

    private final BlogSettingsMapper blogSettingsMapper;

    @Override
    public BlogSettingsVO queryBlogSettings() {
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(null);
        if (Objects.isNull(blogSettingsDO)) {
            throw new BizException("查询博客信息失败");
        }
        BlogSettingsVO blogSettingsVO = BeanUtils.copyBean(blogSettingsDO, BlogSettingsVO.class);
        return blogSettingsVO;
    }
}
