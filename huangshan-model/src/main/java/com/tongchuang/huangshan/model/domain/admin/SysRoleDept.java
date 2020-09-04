package com.tongchuang.huangshan.model.domain.admin;

import lombok.ToString;

/**
 * 角色和部门关联 sys_role_dept
 * 
 * @author fangwenzao
 */
@ToString
public class SysRoleDept
{
    /** 角色ID */
    private Long roleId;
    
    /** 部门ID */
    private String deptId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public String getDeptId()
    {
        return deptId;
    }

    public void setDeptId(String deptId)
    {
        this.deptId = deptId;
    }
}
