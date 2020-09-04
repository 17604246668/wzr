package com.tongchuang.huangshan.admin.controller;


import com.tongchuang.huangshan.admin.annotation.Log;

import com.tongchuang.huangshan.admin.service.ISysDictDataService;
import com.tongchuang.huangshan.admin.service.ISysDictTypeService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.model.domain.admin.SysDictData;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public ResultVO getInfo(@PathVariable Long dictCode)
    {
        return ResultVOUtil.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public ResultVO dictType(@PathVariable String dictType)
    {
        return ResultVOUtil.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultVO add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public ResultVO remove(@PathVariable Long[] dictCodes)
    {
        return ResultVOUtil.success(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
