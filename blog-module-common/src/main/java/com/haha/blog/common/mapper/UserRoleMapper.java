package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dos.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    @Select("SELECT id, user_id, role, create_time FROM t_role where user_id = #{userId}")
    List<UserRoleDO> selectRoleByUserId(@Param("userId") Long userId);

}
