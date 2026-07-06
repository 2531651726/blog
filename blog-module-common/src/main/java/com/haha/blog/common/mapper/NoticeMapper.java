package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dos.NoticeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author li
 * @since 2026-07-06
 */
@Mapper
public interface NoticeMapper extends BaseMapper<NoticeDO> {

}
