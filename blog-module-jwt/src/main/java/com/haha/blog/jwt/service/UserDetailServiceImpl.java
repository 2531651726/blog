package com.haha.blog.jwt.service;

import com.haha.blog.common.domain.dos.UserDO;
import com.haha.blog.common.domain.dos.UserRoleDO;
import com.haha.blog.common.mapper.UserMapper;
import com.haha.blog.common.mapper.UserRoleMapper;
import com.haha.blog.jwt.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询数据
        UserDO userDO = userMapper.findByUsername(username);
        // 判断用户是否存在
        if (Objects.isNull(userDO)) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        Long userId = userDO.getId();
        List<UserRoleDO> roleDOS = roleMapper.selectRoleByUserId(userId);
        /*String[] roleArr = new String[0];
        // 转数组
        if (!CollectionUtils.isEmpty(roleDOS)) {
            roleArr = roleDOS.stream().map(UserRoleDO::getRole).toArray(String[]::new);
        }
        return User.withUsername(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(roleArr)
                .build();*/
        List<SimpleGrantedAuthority> authorityList = roleDOS.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        return LoginUser.builder()
                .id(userId)
                .username(userDO.getUsername())
                .password(userDO.getPassword())
                .authorities(authorityList)
                .build();
    }



}
