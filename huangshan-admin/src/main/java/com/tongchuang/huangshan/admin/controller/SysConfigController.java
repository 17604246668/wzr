package com.tongchuang.huangshan.admin.controller;

import com.tongchuang.huangshan.admin.annotation.Log;
import com.tongchuang.huangshan.admin.service.ISysConfigService;
import com.tongchuang.huangshan.admin.utils.SecurityUtils;
import com.tongchuang.huangshan.common.base.ResultVO;
import com.tongchuang.huangshan.common.constatnts.UserConstants;
import com.tongchuang.huangshan.common.enums.BusinessType;
import com.tongchuang.huangshan.common.enums.ResultEnum;
import com.tongchuang.huangshan.common.utils.ResultVOUtil;
import com.tongchuang.huangshan.model.domain.admin.SysConfig;
import com.tongchuang.huangshan.model.dtos.admin.base.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 * 
 * @author fangwenzao
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public ResultVO getInfo(@PathVariable Long configId)
    {
        return ResultVOUtil.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public ResultVO getConfigKey(@PathVariable String configKey)
    {
        return ResultVOUtil.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ResultVO add(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResultVO edit(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR,"修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return ResultVOUtil.success(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public ResultVO remove(@PathVariable Long[] configIds)
    {
        return ResultVOUtil.success(configService.deleteConfigByIds(configIds));
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public ResultVO clearCache()
    {
        configService.clearCache();
        return ResultVOUtil.success();
    }
}
