package com.tongchuang.huangshan.model.dtos.admin.vo;

import com.tongchuang.huangshan.model.domain.admin.SysUser;
import lombok.Data;

import java.util.Set;

@Data
public class UserInfoVO {

    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;
}
