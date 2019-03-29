package com.hdkj.modules.JW.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hdkj.common.annotation.Encryption;
import com.hdkj.datasources.annotation.DataSource;
import com.hdkj.modules.JW.service.NewApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/NewApi")
@Api(value = "/NewJw", description = "新接口")
public class NewJwController {
    @Autowired
    private NewApiService newApiService;

    /**
     * 3.2	查询用户基本信息（加签名，加密）
     *
     * @param json
     * @return
     */
    @Encryption
    @RequestMapping(value = "/QueryUser", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "查询用户基本信息",notes = "根据输入的参数")
    public String queryUser(@RequestBody String json) throws JsonProcessingException {
        return newApiService.queryUser(json);
    }

    /**
     * 3.3	查询联系、账号、身份证、变更信息（加签名，加密）
     *
     * @param json
     * @return
     */
    @Encryption
    @RequestMapping(value = "/QueryUserBankCertPro", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "查询联系、账号、身份证、变更信息",notes = "根据输入的参数")
    public String queryUserBankCertPro(@RequestBody String json) throws JsonProcessingException {
        return newApiService.queryUserBankCertPro(json);
    }

    /**
     * 3.4	查询电量信息（加签名，加密）
     *
     * @param json
     * @return
     */
    @Encryption
    @RequestMapping(value = "/QueryElectc", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "查询电量信息",notes = "根据输入的参数")
    public String queryElectc(@RequestBody String json) throws JsonProcessingException {
        return newApiService.queryElectc(json);
    }

    /**
     * 3.5	查询缴费信息（加签名，加密）
     *
     * @param json
     * @return
     */
    @Encryption
    @RequestMapping(value = "/QueryPay", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "查询缴费信息",notes = "根据输入的参数")
    public String queryPay(@RequestBody String json) throws JsonProcessingException {
        return newApiService.queryPay(json);
    }

    /**
     * 3.6	查询每日电量信息（加签名，加密）
     *
     * @param json
     * @return
     */
    //@Encryption
   // @RequestMapping(value = "/QueryDaily", method = RequestMethod.POST, produces = "*/*")
   // @ResponseBody
   // public String queryDaily(@RequestBody String json) throws JsonProcessingException {
    //    return newApiService.queryDaily(json);
    //}
}
