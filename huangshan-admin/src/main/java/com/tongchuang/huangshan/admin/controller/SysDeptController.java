package com.tongchuang.huangshan.admin.controller;


import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.service.ISysDeptService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.constatnts.UserConstants;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.common.utils.StringUtils;
import com.tongchuang.huangshan.model.domain.admin.SysDept;
import com.tongchuang.huangshan.model.dtos.admin.base.TreeSelect;
import com.tongchuang.huangshan.model.dtos.admin.vo.RoleDeptSelectVO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public ResultVO list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return ResultVOUtil.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public ResultVO excludeChild(@PathVariable(value = "deptId", required = false) String deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().equals(deptId)
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return ResultVOUtil.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public ResultVO getInfo(@PathVariable String deptId)
    {
        return ResultVOUtil.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public ResultVO treeselect(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return ResultVOUtil.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public ResultVO roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        List<Integer> checkedKeys = deptService.selectDeptListByRoleId(roleId);
        List<TreeSelect> deptsTree = deptService.buildDeptTreeSelect(depts);
        return ResultVOUtil.success(convertToRoleDeptTreeVO(checkedKeys,deptsTree));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultVO add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public ResultVO remove(@PathVariable String deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"部门存在用户,不允许删除");
        }
        return ResultVOUtil.success(deptService.deleteDeptById(deptId));
    }


    private RoleDeptSelectVO convertToRoleDeptTreeVO(List<Integer> checkedKeys, List<TreeSelect> depts){
        RoleDeptSelectVO bean = new RoleDeptSelectVO();
        bean.setCheckedKeys(checkedKeys);
        bean.setDepts(depts);
        return bean;
    }
}
