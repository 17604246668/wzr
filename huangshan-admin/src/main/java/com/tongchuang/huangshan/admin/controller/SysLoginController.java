package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.base.LoginUser;
import com.tongchuang.huangshan.admin.security.SysLoginService;
import com.tongchuang.huangshan.admin.security.SysPermissionService;
import com.tongchuang.huangshan.admin.security.TokenService;
import com.tongchuang.huangshan.admin.service.ISysMenuService;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.common.utils.ServletUtils;
import com.tongchuang.huangshan.model.domain.admin.SysMenu;
import com.tongchuang.huangshan.model.domain.admin.SysUser;
import com.tongchuang.huangshan.model.dtos.admin.dto.LoginBody;
import com.tongchuang.huangshan.model.dtos.admin.vo.TokenVO;
import com.tongchuang.huangshan.model.dtos.admin.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author fangwenzao
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     * 
     * @param loginBody 登陆信息
     * @return 结果
     */
    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginBody loginBody)
    {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        return ResultVOUtil.success(convertToTokenVO(token));
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public ResultVO getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        return ResultVOUtil.success(convertToUserInfoVO(user,roles,permissions));
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public ResultVO getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return ResultVOUtil.success(menuService.buildMenus(menus));
    }


    private TokenVO convertToTokenVO(String token){
        TokenVO bean = new TokenVO();
        bean.setToken(token);
        return bean;
    }

    private UserInfoVO convertToUserInfoVO(SysUser user, Set<String> roles, Set<String> permissions){
        UserInfoVO bean = new UserInfoVO();
        bean.setUser(user);
        bean.setRoles(roles);
        bean.setPermissions(permissions);
        return bean;
    }

}
