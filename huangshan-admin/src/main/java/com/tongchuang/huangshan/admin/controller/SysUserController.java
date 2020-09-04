package com.tongchuang.huangshan.admin.controller;


import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.service.ISysPostService;
import com.tongchuang.huangshan.admin.service.ISysRoleService;
import com.tongchuang.huangshan.admin.service.ISysUserService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.constatnts.UserConstants;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.common.utils.StringUtils;
import com.tongchuang.huangshan.model.domain.admin.SysRole;
import com.tongchuang.huangshan.model.domain.admin.SysUser;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import com.tongchuang.huangshan.model.dtos.admin.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public SysUserVO getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        SysUserVO ajax = new SysUserVO();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.setRoles(SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.setPosts(postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.setData(userService.selectUserById(userId));
            ajax.setPostIds(postService.selectPostListByUserId(userId));
            ajax.setRoleIds(roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultVO add(@RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        /*else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }*/
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return ResultVOUtil.success(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO edit(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        /*if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }*/
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public ResultVO remove(@PathVariable Long[] userIds)
    {
        return ResultVOUtil.success(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public ResultVO resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public ResultVO changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(userService.updateUserStatus(user));
    }
}