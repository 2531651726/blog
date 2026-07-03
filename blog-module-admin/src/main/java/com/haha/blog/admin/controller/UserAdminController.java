package com.haha.blog.admin.controller;


import com.haha.blog.admin.domain.dto.user.UpdateAdminUserPasswordDTO;
import com.haha.blog.admin.service.IUserAdminService;
import com.haha.blog.common.aspect.ApiOperationLog;
import com.haha.blog.admin.domain.vo.user.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(tags = "admin用户模块")
public class UserAdminController {

    private final IUserAdminService userService;

    @PostMapping("/password/update")
    @ApiOperation("修改用户密码")
    @ApiOperationLog(description = "修改用户密码")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePassword(@RequestBody @Validated UpdateAdminUserPasswordDTO vo){
        userService.updatePassword(vo);
    }

    @GetMapping("/user/info")
    @ApiOperation("获取登录用户信息")
    @ApiOperationLog(description = "获取登录用户信息")
    public UserInfoVO getUserInfo(){
        return userService.getUserInfo();
    }

}
