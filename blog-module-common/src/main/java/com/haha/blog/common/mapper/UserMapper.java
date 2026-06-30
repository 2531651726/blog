package com.haha.blog.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haha.blog.common.domain.dos.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
    
    @Select("SELECT id, username, password, create_time, update_time, is_deleted FROM t_user where username = #{username} and is_deleted = 0")
    UserDO findByUsername(@Param("username") String username);


}
