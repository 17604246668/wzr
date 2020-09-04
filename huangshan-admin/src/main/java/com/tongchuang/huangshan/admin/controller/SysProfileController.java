package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.base.LoginUser;
import com.tongchuang.huangshan.admin.security.TokenService;
import com.tongchuang.huangshan.admin.service.ISysUserService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.common.utils.ServletUtils;
import com.tongchuang.huangshan.model.domain.admin.SysUser;
import com.tongchuang.huangshan.model.dtos.admin.vo.ProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息 业务处理
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 个人信息
     */
    @GetMapping
    public ResultVO profile()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        String roleGroup = userService.selectUserRoleGroup(loginUser.getUsername());
        String postGroup = userService.selectUserPostGroup(loginUser.getUsername());
        return ResultVOUtil.success(convertToProfileVO(user,roleGroup,postGroup));
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO updateProfile(@RequestBody SysUser user)
    {
        if (userService.updateUserProfile(user) > 0)
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return ResultVOUtil.success();
        }
        return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public ResultVO updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return ResultVOUtil.success();
        }
        return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public ResultVO avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            //String avatar = FileUploadUtils.upload(fangwenzaoConfig.getAvatarPath(), file);
            /*if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
            {
                ResultVO ajax = ResultVOUtil.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }*/
        }
        return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"上传图片异常，请联系管理员");
    }

    private ProfileVO convertToProfileVO(SysUser user, String roleGroup, String postGroup){
        ProfileVO bean = new ProfileVO();
        bean.setUser(user);
        bean.setPostGroup(postGroup);
        bean.setRoleGroup(roleGroup);
        return bean;
    }
}
