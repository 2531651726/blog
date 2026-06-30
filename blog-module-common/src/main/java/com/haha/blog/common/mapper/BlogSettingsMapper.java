package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dos.BlogSettingsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogSettingsMapper extends BaseMapper<BlogSettingsDO> {
}
