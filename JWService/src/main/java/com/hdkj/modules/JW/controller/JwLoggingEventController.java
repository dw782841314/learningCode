package com.hdkj.modules.JW.controller;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdkj.common.utils.PageUtils;
import com.hdkj.common.utils.R;
import com.hdkj.common.validator.ValidatorUtils;
import com.hdkj.datasources.annotation.DataSource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hdkj.modules.JW.entity.JwLoggingEventEntity;
import com.hdkj.modules.JW.service.JwLoggingEventService;


/**
 * ${comments}
 *
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:18
 */
@RestController
@RequestMapping("JW/jwloggingevent")
@Api(value = "/LoggingEvent", description = "日志信息")
public class JwLoggingEventController {
    @Autowired
    private JwLoggingEventService jwLoggingEventService;


    /**
     * 列表
     */

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("JW:jwloggingevent:list")
    @ApiOperation(value = "所有日志列表",notes = "根据输入的参数")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = jwLoggingEventService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */

    @RequestMapping(value = "/info/{logId}",method = RequestMethod.GET)
    @RequiresPermissions("JW:jwloggingevent:info")
    @ApiOperation(value = "根据ID查询日志列表",notes = "根据输入的参数")
    public R info(@PathVariable("logId") String logId) {
        JwLoggingEventEntity jwLoggingEvent = jwLoggingEventService.selectById(logId);
        return R.ok().put("jwLoggingEvent", jwLoggingEvent);
    }


    /**
     * 保存
     */

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwloggingevent:save")
    @ApiOperation(value = "新增日志",notes = "根据输入的参数")
    public R save(@RequestBody JwLoggingEventEntity jwLoggingEvent) {
        jwLoggingEventService.insert(jwLoggingEvent);
        return R.ok();
    }


    /**
     * 修改
     */

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwloggingevent:update")
    @ApiOperation(value = "修改日志",notes = "根据输入的参数")
    public R update(@RequestBody JwLoggingEventEntity jwLoggingEvent) {
        ValidatorUtils.validateEntity(jwLoggingEvent);
        jwLoggingEventService.updateAllColumnById(jwLoggingEvent);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwloggingevent:delete")
    @ApiOperation(value = "删除日志",notes = "根据输入的参数")
    public R delete(@RequestBody byte[] logIds) {
        jwLoggingEventService.deleteBatchIds(Arrays.asList(logIds));

        return R.ok();
    }


}
