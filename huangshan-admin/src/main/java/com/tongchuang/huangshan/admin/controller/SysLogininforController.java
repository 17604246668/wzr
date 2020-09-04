package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.service.ISysLogininforService;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.model.domain.admin.SysLogininfor;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登陆日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public ResultVO remove(@PathVariable Long[] infoIds)
    {
        return ResultVOUtil.success(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登陆日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public ResultVO clean()
    {
        logininforService.cleanLogininfor();
        return ResultVOUtil.success();
    }
}
