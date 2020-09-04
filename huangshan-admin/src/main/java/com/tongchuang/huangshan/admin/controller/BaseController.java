package com.tongchuang.huangshan.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.SqlUtil;
import com.tongchuang.huangshan.common.utils.StringUtils;
import com.tongchuang.huangshan.model.dtos.admin.base.PageDomain;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import com.tongchuang.huangshan.model.dtos.admin.base.TableSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * web层通用数据处理
 * 
 * @author fangwenzao
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(ResultEnum.SUCCESS.getCode());
        rspData.setMsg(ResultEnum.SUCCESS.getMessage());
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
}
