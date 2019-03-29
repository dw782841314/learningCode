package com.hdkj.modules.JW.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hdkj.common.utils.PageUtils;
import com.hdkj.common.utils.R;
import com.hdkj.common.validator.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hdkj.modules.JW.entity.JwDigitCertEntity;
import com.hdkj.modules.JW.service.JwDigitCertService;



/**
 * 证书标识表
 *
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:19
 */
@RestController
@RequestMapping("JW/jwdigitcert")
@Api(value = "/DigitCert", description = "证书信息")
public class JwDigitCertController {
    @Autowired
    private JwDigitCertService jwDigitCertService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("JW:jwdigitcert:list")
    @ApiOperation(value = "所有证书列表",notes = "根据输入的参数")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jwDigitCertService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value = "/info/{digitId}",method = RequestMethod.GET)
    @RequiresPermissions("JW:jwdigitcert:info")
    @ApiOperation(value = "查询证书列表",notes = "根据输入的参数")
    public R info(@PathVariable("digitId") String digitId){
        JwDigitCertEntity jwDigitCert = jwDigitCertService.selectById(digitId);

        return R.ok().put("jwDigitCert", jwDigitCert);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwdigitcert:save")
    @ApiOperation(value = "添加证书列表",notes = "根据输入的参数")
    public R save(@RequestBody JwDigitCertEntity jwDigitCert){
        jwDigitCertService.insert(jwDigitCert);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwdigitcert:update")
    @ApiOperation(value = "修改证书信息",notes = "根据输入的参数")
    public R update(@RequestBody JwDigitCertEntity jwDigitCert){
        ValidatorUtils.validateEntity(jwDigitCert);
        jwDigitCertService.updateAllColumnById(jwDigitCert);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @RequiresPermissions("JW:jwdigitcert:delete")
    @ApiOperation(value = "删除证书信息",notes = "根据输入的参数")
    public R delete(@RequestBody String[] digitIds){
        jwDigitCertService.deleteCert(Arrays.asList(digitIds));

        return R.ok();
    }

}
