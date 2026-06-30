package com.haha.blog.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haha.blog.admin.domain.dto.User.UpdateAdminUserPasswordDTO;
import com.haha.blog.common.domain.dos.UserDO;
import com.haha.blog.admin.domain.vo.user.UserInfoVO;

public interface IUserAdminService extends IService<UserDO> {

    void updatePassword(UpdateAdminUserPasswordDTO vo);

    UserInfoVO getUserInfo();

    UserInfoVO queryAdminDetail();
}
