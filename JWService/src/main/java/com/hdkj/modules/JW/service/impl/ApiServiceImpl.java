package com.hdkj.modules.JW.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hdkj.common.utils.DateUtils;
import com.hdkj.common.utils.FileUtils;
import com.hdkj.modules.JW.dao.ElecInfoDao;
import com.hdkj.modules.JW.entity.JwLoggingEventEntity;
import com.hdkj.modules.JW.entity.UserProfiles;
import com.hdkj.modules.JW.service.ApiService;
import com.hdkj.modules.JW.service.JwLoggingEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.util.*;

@Service("ApiService")
public class ApiServiceImpl implements ApiService {
    private static Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    @Autowired
    private ElecInfoDao elecInfoMapper;
    /* @Resource(name = "loggingEventService")
     private LoggingEventService loggingEventService;*/
    @Autowired
    private JwLoggingEventService jwLoggingEventService;

    public String query(String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        jwLoggingEventEntity.setOperation("加签名加密或加密不加签名");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("json校验失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****失败出参****json验证失败");
            return "1001";
        }
        String acctName = (String) map.get("ACCT_NAME");
        String consNo = (String) map.get("CERT_NO");
        String timeStamp = (String) map.get("TIME_STAMP");
        String appid = (String) map.get("APPID");
        String digitCert = (String) map.get("DIGIT_CERT");
        jwLoggingEventEntity.setParams("ACCT_NAME:" + acctName + ",CERT_NO:" + consNo + ",TIME_STAMP:" + timeStamp + ",APPID:" + appid);
        jwLoggingEventEntity.setUserId(appid);
        if (consNo == null || acctName == null || timeStamp == null || acctName == null || appid == null) {
            try {
                jwLoggingEventEntity.setReturnParams("参数不正确");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败******");
            }
            return "1004";
        }
        try {
            //获取图片并保存到本地
            if (digitCert != null) {
                if (digitCert.contains("data:image")) {
                    String[] strs = digitCert.split(",");
                    digitCert = strs[1];
                    BASE64Decoder decoder = new BASE64Decoder();
                    //loggingEvent.setDigit_cert(decoder.decodeBuffer(digitCert));
                    FileUtils.base64StrToImage(digitCert, "/var/img/" + new Date().getTime() + ".jpg");
                }
            }
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("图片存储失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****证书图片保存失败****");
        }

        List<Map<String, String>> cust = new ArrayList<Map<String, String>>();
        try {
            cust = elecInfoMapper.getCust(acctName, consNo);
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("用户查询失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            return "1013";
        }
        if (cust.size() < 1) {
            jwLoggingEventEntity.setReturnParams("用户未找到");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            return "1014";
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
                    return "1006";
                }
                log.info("****用户档案查询完成****");
                try {
                    userProfiles.setACCT_LIST(elecInfoMapper.getAccountInfo(parameter.get("CONS_NO")));
                } catch (Exception e) {
                    jwLoggingEventEntity.setReturnParams("银行账号信息查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    return "1007";
                }
                log.info("****银行账号信息查询完成****");
                try {
                    userProfiles.setCERT_LIST(elecInfoMapper.getCertificateInfo(parameter.get("CONS_NO")));
                } catch (Exception e) {
                    jwLoggingEventEntity.setReturnParams("证件信息查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    return "1008";
                }
                log.info("****证件信息查询完成****");
                try {
                    userProfiles.setELECTC_LIST(elecInfoMapper.getElecQuery(parameter.get("CONS_NO"), parameter.get("ORG_NO")));
                } catch (Exception e) {
                    jwLoggingEventEntity.setReturnParams("应收电量查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    return "1009";
                }
                log.info("****应收电量查询完成****");
                try {
                    userProfiles.setMOBILE(elecInfoMapper.getPhone(parameter.get("CONS_NO")));
                } catch (Exception e) {
                    jwLoggingEventEntity.setReturnParams("联系电话查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    return "1010";
                }
                log.info("****联系电话查询完成****");
                try {
                    userProfiles.setRCV_LIST(elecInfoMapper.getPaymentRec(parameter.get("CONS_NO"), parameter.get("ORG_NO")));
                } catch (Exception e) {
                    jwLoggingEventEntity.setReturnParams("缴费方式查询失败");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    return "1011";
                }
                log.info("****缴费方式查询完成****");
                data.add(userProfiles);
            } else {
                try {
                    jwLoggingEventEntity.setReturnParams("用户编号或单位编号未找到");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("********日志存储失败******");
                }
                return "1012";
            }
        }
        System.out.println("=========================结束=========================");
        Map<String, Object> map1 = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        map1.put("data", data);
        map1.put("code", "0");
        try {
            jwLoggingEventEntity.setReturnParams(gson.toJson(data));
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
        } catch (Exception e) {
            log.info("*******存储日志失败*****");
        }
        log.info("****成功出参****" + objectMapper.writeValueAsString(map1));
        return objectMapper.writeValueAsString(map1);
    }
}
