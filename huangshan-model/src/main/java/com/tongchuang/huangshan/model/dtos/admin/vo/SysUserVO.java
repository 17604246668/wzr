package com.tongchuang.huangshan.model.dtos.admin.vo;

import com.tongchuang.huangshan.model.domain.admin.SysPost;
import com.tongchuang.huangshan.model.domain.admin.SysRole;
import com.tongchuang.huangshan.model.domain.admin.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class SysUserVO {

    private String code;

    private String msg;

    List<SysRole> roles;

    List<SysPost> posts;

    List<String> postIds;

    List<Integer> roleIds;

    SysUser data;
}
