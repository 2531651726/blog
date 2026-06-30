package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.common.domain.vo.BlogSettings.BlogSettingsVO;
import com.haha.blog.common.mapper.BlogSettingsMapper;
import com.haha.blog.admin.service.IBlogSettingsAdminService;
import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.haha.blog.admin.domain.dto.BlogSettings.UpdateBlogSettingsDTO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogSettingsAdminServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements IBlogSettingsAdminService {

    private final BlogSettingsMapper blogSettingsMapper;

    @Override
    public void updateBlogSettings(UpdateBlogSettingsDTO dto) {
        Long id = 1L;
        BlogSettingsDO blogSettingsDO = BeanUtils.copyBean(dto, BlogSettingsDO.class);
        blogSettingsDO.setId(id);
        boolean success = saveOrUpdate(blogSettingsDO);
        if (!success) {
            throw new BizException("更新博客设置失败");
        }
    }

    @Override
    public BlogSettingsVO findDetail() {
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(null);
        if (Objects.isNull(blogSettingsDO)) {
            throw new BizException("查询博客设置失败");
        }
        BlogSettingsVO blogSettingsVO = BeanUtils.copyBean(blogSettingsDO, BlogSettingsVO.class);
        return blogSettingsVO;
    }
}
