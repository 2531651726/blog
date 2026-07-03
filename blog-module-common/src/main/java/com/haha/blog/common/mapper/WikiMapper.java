package com.haha.blog.common.mapper;

import com.haha.blog.common.domain.dos.WikiCatalogDO;
import com.haha.blog.common.domain.dos.WikiDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 知识库表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-07-01
 */
@Mapper
public interface WikiMapper extends BaseMapper<WikiDO> {



}
