package com.hdkj.modules.JW.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hdkj.common.annotation.Decode;
import com.hdkj.common.annotation.Encryption;
import com.hdkj.common.utils.Constant;
import com.hdkj.common.utils.DateUtils;
import com.hdkj.common.utils.FileUtils;
import com.hdkj.modules.JW.dao.ElecInfoDao;
import com.hdkj.modules.JW.entity.JwLoggingEventEntity;
import com.hdkj.modules.JW.entity.UserProfiles;
import com.hdkj.modules.JW.service.ApiService;
import com.hdkj.modules.JW.service.JwLoggingEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.util.*;


/**
 * 主接口
 *
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:19
 */
@RestController
@RequestMapping("/ElecInfo")
@Api(value = "/JW", description = "旧接口")
public class JwController {
    private static Logger log = LoggerFactory.getLogger(JwController.class);
    @Autowired
    private ElecInfoDao elecInfoMapper;
    @Autowired
    ApiService apiService;
    @Autowired
    JwLoggingEventService jwLoggingEventService;

    /**
     * 不加签名，不加密
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/Query", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "不加签名，不加密",notes = "根据输入的参数")
    public String query2(@RequestBody String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setOperation("不加签名，不加密");
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        Map<String, Object> msg = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                map = gson.fromJson(json, map.getClass());
            } catch (Exception e) {
                jwLoggingEventEntity.setReturnParams("json解析失败");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
                msg.put("code", "1001");
                msg.put("msg", Constant.getCode().get("1001"));
                return objectMapper.writeValueAsString(msg);
            }
            //用户名
            String acctName = (String) map.get("ACCT_NAME");
            //用户证件号
            String consNo = (String) map.get("CERT_NO");
            //当前时间戳
            String timeStamp = (String) map.get("TIME_STAMP");
            //请求方标识
            String appid = (String) map.get("APPID");
            //证书文件
            String digitCert = (String) map.get("DIGIT_CERT");
            jwLoggingEventEntity.setUserId(appid);
            jwLoggingEventEntity.setParams("ACCT_NAME:" + acctName + ",consNo:" + consNo + ",timeStamp:" + timeStamp + ",APPID:" + appid);
            if (consNo == null || acctName == null || timeStamp == null || acctName == null || appid == null) {
                msg.put("code", "1004");
                msg.put("msg", Constant.getCode().get("1004"));
                try {
                    jwLoggingEventEntity.setReturnParams("参数不正确");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("******日志存储失败*****");
                }
                return objectMapper.writeValueAsString(msg);
            }
            try {
                //获取图片并保存到本地
                if (digitCert != null) {
                    if (digitCert.contains("data:image")) {
                        String[] strs = digitCert.split(",");
                        digitCert = strs[1];
                        BASE64Decoder decoder = new BASE64Decoder();
                        FileUtils.base64StrToImage(digitCert, "/var/img/" + new Date().getTime() + ".jpg");
                    }
                }
            } catch (Exception e) {
                jwLoggingEventEntity.setReturnParams("证书图片保存失败");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
                log.info("****证书图片保存失败****");
            }
            List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
            try {
                cust = elecInfoMapper.getCust(acctName, consNo);
            } catch (Exception e) {
                jwLoggingEventEntity.setReturnParams("解密失败");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
                msg.put("code", "1013");
                msg.put("msg", Constant.getCode().get("1013"));
                return objectMapper.writeValueAsString(msg);
            }
            log.info("****用户信息查询完成****");
            if (cust.size() < 1) {
                try {
                    jwLoggingEventEntity.setReturnParams("用户查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("*******日志存储失败******");
                }
                msg.put("code", "1014");
                msg.put("msg", Constant.getCode().get("1014"));
                return objectMapper.writeValueAsString(msg);
            }
            System.out.println("========================开始==========================");
            List<UserProfiles> data = new ArrayList<UserProfiles>();
            for (Map<String, String> parameter : cust) {
                System.out.println(parameter);
                if (parameter.get("CONS_NO") != null && parameter.get("ORG_NO") != null) {
                    UserProfiles userProfiles = new UserProfiles();
                    try {
                        userProfiles = elecInfoMapper.getUserProfiles(parameter.get("CONS_NO"));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("用户档案查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1006");
                        msg.put("msg", Constant.getCode().get("1006"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****用户档案查询完成****");
                    try {
                        userProfiles.setACCT_LIST(elecInfoMapper.getAccountInfo(parameter.get("CONS_NO")));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("银行账号查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1007");
                        msg.put("msg", Constant.getCode().get("1007"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****银行账号信息查询完成****");
                    try {
                        userProfiles.setCERT_LIST(elecInfoMapper.getCertificateInfo(parameter.get("CONS_NO")));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("证件信息查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1008");
                        msg.put("msg", Constant.getCode().get("1008"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****证件信息查询完成****");
                    try {
                        userProfiles.setELECTC_LIST(elecInfoMapper.getElecQuery(parameter.get("CONS_NO"), parameter.get("ORG_NO")));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("应收电量查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1009");
                        msg.put("msg", Constant.getCode().get("1009"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****应收电量查询完成****");
                    try {
                        userProfiles.setMOBILE(elecInfoMapper.getPhone(parameter.get("CONS_NO")));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("联系电话查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1010");
                        msg.put("msg", Constant.getCode().get("1010"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****联系电话查询完成****");
                    try {
                        userProfiles.setRCV_LIST(elecInfoMapper.getPaymentRec(parameter.get("CONS_NO"), parameter.get("ORG_NO")));
                    } catch (Exception e) {
                        jwLoggingEventEntity.setReturnParams("缴费方式查询失败");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        msg.put("code", "1011");
                        msg.put("msg", Constant.getCode().get("1011"));
                        return objectMapper.writeValueAsString(msg);
                    }
                    log.info("****缴费方式查询完成****");
                    data.add(userProfiles);
                } else {
                    try {
                        jwLoggingEventEntity.setReturnParams("用户编号或单位编号未找到");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    } catch (Exception e) {
                        log.info("*******日志存储失败******");
                    }
                    msg.put("code", "1012");
                    msg.put("msg", Constant.getCode().get("1012"));
                    return objectMapper.writeValueAsString(msg);
                }
            }
            System.out.println("=========================结束=========================");
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("data", data);
            map1.put("code", "0");
            log.info("****成功出参****" + objectMapper.writeValueAsString(map1));
            try {
                jwLoggingEventEntity.setReturnParams(gson.toJson(data));
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("********日志存储失败******");
            }
            return objectMapper.writeValueAsString(map1);
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("未知异常");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            msg.put("code", "1");
            msg.put("msg", Constant.getCode().get("1"));
            return objectMapper.writeValueAsString(msg);
        }
    }

    /**
     * 加签名，不加密
     *
     * @param json
     * @return
     */
    @Decode
    @RequestMapping(value = "/QueryWithSign", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "加签名，不加密",notes = "根据输入的参数")
    public String query(@RequestBody String json) throws JsonProcessingException {
        return apiService.query(json);
    }

    /**
     * 加签名，加密
     *
     * @param json
     * @return
     */
    @Encryption
    @RequestMapping(value = "/QueryWithEncrpt", method = RequestMethod.POST, produces = "*/*")
    @ResponseBody
    @ApiOperation(value = "加签名，加密",notes = "根据输入的参数")
    public String query3(@RequestBody String json) throws JsonProcessingException {
        return apiService.query(json);
    }


}
