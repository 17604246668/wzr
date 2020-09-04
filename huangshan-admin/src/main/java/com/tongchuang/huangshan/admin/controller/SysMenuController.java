package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.base.LoginUser;
import com.tongchuang.huangshan.admin.security.TokenService;
import com.tongchuang.huangshan.admin.service.ISysMenuService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.constatnts.Constants;
import com.tongchuang.huangshan.common.constatnts.UserConstants;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.common.utils.ServletUtils;
import com.tongchuang.huangshan.common.utils.StringUtils;
import com.tongchuang.huangshan.model.domain.admin.SysMenu;
import com.tongchuang.huangshan.model.dtos.admin.base.TreeSelect;
import com.tongchuang.huangshan.model.dtos.admin.vo.RoleTreeSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public ResultVO list(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return ResultVOUtil.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public ResultVO getInfo(@PathVariable Long menuId)
    {
        return ResultVOUtil.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResultVO treeselect(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return ResultVOUtil.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResultVO roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getUserId());
        List<Integer> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<TreeSelect> treeSelects = menuService.buildMenuTreeSelect(menus);
        return ResultVOUtil.success(convertToRoleTreeSelectVO(checkedKeys,treeSelects));
    }

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultVO add(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO edit(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public ResultVO remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"菜单已分配,不允许删除");
        }
        return ResultVOUtil.success(menuService.deleteMenuById(menuId));
    }


    private RoleTreeSelectVO convertToRoleTreeSelectVO(List<Integer> checkedKeys, List<TreeSelect> menus){
        RoleTreeSelectVO bean = new RoleTreeSelectVO();
        bean.setCheckedKeys(checkedKeys);
        bean.setMenus(menus);
        return bean;
    }

}