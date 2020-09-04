package com.tongchuang.huangshan.model.dtos.admin.base;

import com.tongchuang.huangshan.common.constatnts.Constants;
import lombok.Data;

@Data
public class PageDTO {

    private Integer pageNum = Constants.DEFAULT_PAGE_NUM;

    private Integer pageSize = Constants.DEFAULT_PAGE_SIZE;
}
