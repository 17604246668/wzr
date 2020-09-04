package com.tongchuang.huangshan.model.dtos.admin.vo;

import com.tongchuang.huangshan.model.dtos.admin.base.TreeSelect;
import lombok.Data;

import java.util.List;

@Data
public class RoleDeptSelectVO {

    private List<Integer> checkedKeys;

    private List<TreeSelect> depts;
}
