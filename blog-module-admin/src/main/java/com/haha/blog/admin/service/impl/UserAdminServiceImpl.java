package com.haha.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haha.blog.admin.domain.dto.user.UpdateAdminUserPasswordDTO;
import com.haha.blog.admin.service.IUserAdminService;
import com.haha.blog.common.domain.dos.BlogSettingsDO;
import com.haha.blog.common.domain.dos.UserDO;
import com.haha.blog.admin.domain.vo.user.UserInfoVO;
import com.haha.blog.common.exception.BizException;
import com.haha.blog.common.mapper.BlogSettingsMapper;
import com.haha.blog.common.mapper.UserMapper;
import com.haha.blog.jwt.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl extends ServiceImpl<UserMapper, UserDO> implements IUserAdminService {

    private final UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private final BlogSettingsMapper blogSettingsMapper;

    @Override
    public void updatePassword(UpdateAdminUserPasswordDTO vo) {
        String username = vo.getUsername();
        String password = vo.getPassword();
        if(!Objects.equals(username, UserUtils.getCurrentUsername())){
            log.debug("当前操作用户名：{}，修改的用户名{}", UserUtils.getCurrentUsername(), username);
            throw new BizException("不允许修改他人密码");
        }
        // 加密密码
        String encodePassword = passwordEncoder.encode(password);
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(UserDO::getPassword,encodePassword)
                .eq(UserDO::getUsername,username);
        int row = userMapper.update(null, wrapper);
        if(row != 1){
            throw new BizException("用户密码更新失败，用户不存在");
        }

    }

    @Override
    public UserInfoVO getUserInfo() {
        String username = UserUtils.getUsername();
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(null);
        System.out.println(username);
        System.out.println(blogSettingsDO.getAvatar());
        return UserInfoVO.builder()
                .username(username)
                .avatar(blogSettingsDO.getAvatar())
                .build();
    }
}
