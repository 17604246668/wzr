package com.tongchuang.huangshan.model.dtos.admin.vo;

import com.tongchuang.huangshan.model.domain.admin.SysUser;
import lombok.Data;

@Data
public class ProfileVO {

    private SysUser user;

    private String roleGroup;

    private String postGroup;

}
