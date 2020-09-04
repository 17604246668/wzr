package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.service.ISysOperLogService;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.model.domain.admin.SysOperLog;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController
{
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog)
    {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public ResultVO remove(@PathVariable Long[] operIds)
    {
        return ResultVOUtil.success(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public ResultVO clean()
    {
        operLogService.cleanOperLog();
        return ResultVOUtil.success();
    }
}
