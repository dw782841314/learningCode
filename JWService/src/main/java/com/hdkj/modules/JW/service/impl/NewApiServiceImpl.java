package com.hdkj.modules.JW.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hdkj.common.utils.DateUtils;
import com.hdkj.common.utils.FileUtils;
import com.hdkj.datasources.annotation.DataSource;
import com.hdkj.modules.JW.dao.NewApiDao;
import com.hdkj.modules.JW.entity.*;
import com.hdkj.modules.JW.service.JwDigitCertService;
import com.hdkj.modules.JW.service.JwLoggingEventService;
import com.hdkj.modules.JW.service.NewApiService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.util.*;

@Service("NewApiService")
public class NewApiServiceImpl implements NewApiService {
    private static Logger log = LoggerFactory.getLogger(NewApiServiceImpl.class);

    @Autowired
    private JwDigitCertService jwDigitCertService;
    @Autowired
    private NewApiDao newApiDao;
    @Autowired
    private JwLoggingEventService jwLoggingEventService;

    //查询用户基本信息
    @Override
    public String queryUser(String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setOperation("查询用户基本信息");
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("json验证失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****进参****json验证失败");
            return "1001";
        }
        //用户名称
        String consName = (String) map.get("CONS_NAME");
        //用户证件号
        String certNo = (String) map.get("CERT_NO");
        //手机号
        String mobile = (String) map.get("MOBILE");
        //家庭住址
        String elecAddr = (String) map.get("ELEC_ADDR");
        //当前时间戳
        String timeStamp = (String) map.get("TIME_STAMP");
        //请求方标识
        String appid = (String) map.get("APPID");
        //证书文件
        String digitCert = (String) map.get("DIGIT_CERT");
        //证书文件标识
        String digitCertNo = (String) map.get("DIGIT_CERT_NO");
        //查询页
        String pageNo = (String) map.get("PAGE_NO");
        //每页查询数量
        String page = (String) map.get("PAGE");

        jwLoggingEventEntity.setParams("CONS_NAME:" + consName + ",CERT_NO:" + certNo + ",MOBILE:" + mobile + ",elecAddr:" + elecAddr + ",TIME_STAMP:" + timeStamp + ",appid:" +
                appid + ",pageNo:" + pageNo + ",page:" + page + ",digitCertNo:" + digitCertNo);
        jwLoggingEventEntity.setUserId(appid);
        //证书的UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if(StringUtils.isBlank(digitCert) && StringUtils.isBlank(digitCertNo)){
            try {
                jwLoggingEventEntity.setReturnParams("证书及证书标识均为空");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1019";
        }
        if (StringUtils.isBlank(consName) && StringUtils.isBlank(certNo) && StringUtils.isBlank(mobile) && StringUtils.isBlank(elecAddr)) {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        if (StringUtils.isBlank(timeStamp)  || StringUtils.isBlank(appid)) {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            return "1004";
        }
        if (StringUtils.isBlank(page)) {
            page = "10";
        }
        if (StringUtils.isBlank(pageNo)) {
            pageNo = "1";
        }
        try {
            if (StringUtils.isNotBlank(digitCertNo)) {
                EntityWrapper entityWrapper = new EntityWrapper();
                entityWrapper.eq("digit_cert_no", digitCertNo);
                JwDigitCertEntity jwDigitCertEntity = jwDigitCertService.selectOn(entityWrapper);
                if (jwDigitCertEntity != null) {
                    long s = ((new Date().getTime() - jwDigitCertEntity.getUpdateTime().getTime()) / (1000 * 60 * 60));
                    if (s > 24) {
                        try {
                            jwLoggingEventEntity.setReturnParams("证书失效");
                            jwLoggingEventService.insertInto(jwLoggingEventEntity);
                        } catch (Exception e) {
                            log.info("*******日志存储失败*****");
                        }
                        return "1016";
                    }
                    jwDigitCertEntity.setUpdateTime(new Date());
                    jwDigitCertService.newUpdata(jwDigitCertEntity);
                    jwLoggingEventEntity.setDigitId(jwDigitCertEntity.getDigitId());
                    jwLoggingEventEntity.setIsDigit(0);
                } else {
                    try {
                        jwLoggingEventEntity.setReturnParams("证书无效");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    } catch (Exception e) {
                        log.info("*******日志存储失败*****");
                    }
                    return "1015";
                }
            } else if (StringUtils.isNotBlank(digitCert)) {
                //获取图片并保存到本地
                if (digitCert.contains("data:image")) {
                    String[] strs = digitCert.split(",");
                    digitCert = strs[1];
                    BASE64Decoder decoder = new BASE64Decoder();
                    //loggingEvent.setDigit_cert(decoder.decodeBuffer(digitCert));
                    FileUtils.base64StrToImage(digitCert, "/var/img/" + new Date().getTime() + ".jpg");
                    //存储图片到数据库
                    //数据库ID 存储LOGGING日志需用到
                    String digitId = UUID.randomUUID().toString().replaceAll("-", "");
                    JwDigitCertEntity jwDigitCertEntity = new JwDigitCertEntity();
                    jwDigitCertEntity.setDigitId(digitId);
                    jwDigitCertEntity.setDigitCert(decoder.decodeBuffer(digitCert));
                    jwDigitCertEntity.setDigitCertNo(uuid);
                    jwDigitCertEntity.setCreateTime(new Date());
                    jwDigitCertEntity.setUpdateTime(new Date());
                    jwDigitCertService.insertInto(jwDigitCertEntity);
                    jwLoggingEventEntity.setDigitId(digitId);
                    jwLoggingEventEntity.setIsDigit(1);
                }
            }else {
                try {
                    jwLoggingEventEntity.setReturnParams("参数错误");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("*******日志存储失败*******");
                }
                return "1004";
            }
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("证书图片保存失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****证书图片保存失败****");
            log.info("*********"+e.getMessage());
        }

        try {
            long startTime = System.currentTimeMillis();
            List<Map<String,Object>> count = newApiDao.queryCount(consName,certNo,mobile,elecAddr);
            List<Map<String, Object>> userProfiles = newApiDao.queryUserProfiles(consName, certNo, mobile, elecAddr, (Integer.parseInt(pageNo) - 1) * Integer.parseInt(page), Integer.parseInt(page) * Integer.parseInt(pageNo));
            long endTime = System.currentTimeMillis();
            log.info("*****程序执行时间*****："+(endTime - startTime) + "ms");
            Map<String, Object> map1 = new HashMap<String, Object>();
            ObjectMapper objectMapper = new ObjectMapper();
            if (userProfiles.size() != 0 && userProfiles != null) {
                for (int i = 0; i < userProfiles.size(); i++) {
                    userProfiles.get(i).put("DIGIT_CERT_NO", uuid);
                    userProfiles.get(i).put("TOTAL", count.size());
                }
            } else {
                Map<String, Object> map2 = new HashMap();
                map2.put("DIGIT_CERT_NO", uuid);
                map2.put("TOTAL", Math.round(userProfiles.size()));
                userProfiles.add(map2);
            }
            map1.put("data", userProfiles);
            map1.put("code", "0");
            try {
                jwLoggingEventEntity.setReturnParams(gson.toJson(userProfiles));
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("****日志存储失败*****");
            }
            return objectMapper.writeValueAsString(map1);
        } catch (Exception e) {
            log.info(e.getMessage());
            return "1006";
        }

    }

    //3.3	查询联系、账号、身份证、变更信息
    @Override
    public String queryUserBankCertPro(String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setOperation("查询联系、账号、身份证、变更信息");
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        //证书的UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        // LoggingEventEntity loggingEvent = new LoggingEventEntity();
        //loggingEvent.setParams(json);
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("json验证失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****进参****json验证失败");
            return "1001";
        }
        //用户编号
        String consNo = (String) map.get("CONS_NO");
        //供电所编号
        String orgCode = (String) map.get("ORG_CODE");
        //当前时间戳
        String timeStamp = (String) map.get("TIME_STAMP");
        //请求方标识
        String appid = (String) map.get("APPID");
        //证书文件标识
        String digitCertNo = (String) map.get("DIGIT_CERT_NO");
        jwLoggingEventEntity.setUserId(appid);
        jwLoggingEventEntity.setParams("CONS_NO:" + consNo + ",ORG_CODE:" + orgCode + ",TIME_STAMP:" + timeStamp + ",APPID:" + appid + ",DIGIT_CERT_NO:" + digitCertNo);
        if (StringUtils.isBlank(consNo) || StringUtils.isBlank(orgCode) || StringUtils.isBlank(timeStamp) || StringUtils.isBlank(appid) || StringUtils.isBlank(digitCertNo)) {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        if (StringUtils.isNotBlank(digitCertNo)) {
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.eq("digit_cert_no", digitCertNo);
            JwDigitCertEntity jwDigitCertEntity = jwDigitCertService.selectOn(entityWrapper);
            if (jwDigitCertEntity != null) {
                long s = ((new Date().getTime() - jwDigitCertEntity.getUpdateTime().getTime()) / (1000 * 60 * 60));
                if (s > 24) {
                    try {
                        jwLoggingEventEntity.setReturnParams("证书失效");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    } catch (Exception e) {
                        log.info("*******日志存储失败*****");
                    }
                    return "1016";
                }
                jwDigitCertEntity.setUpdateTime(new Date());
                jwDigitCertService.newUpdata(jwDigitCertEntity);
                jwLoggingEventEntity.setDigitId(jwDigitCertEntity.getDigitId());
                jwLoggingEventEntity.setIsDigit(0);
            } else {
                try {
                    jwLoggingEventEntity.setReturnParams("证书无效");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("*******日志存储失败*****");
                }
                return "1015";
            }
        }else {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        List mapList = new ArrayList<>();
        try {
            //查询变更信息
            List<Map<String, Object>> alterations = newApiDao.queryAlteration(consNo);
            mapList.add(alterations);
            log.info("******变更信息查询完成******");
        } catch (Exception e) {
            log.info("******变更信息查询失败******");
            log.info(e.getMessage());
            jwLoggingEventEntity.setReturnParams("变更信息查询失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            return "1018";
        }
        try {
            //查询银行信息
            List<Map<String, Object>> accountInfoList = newApiDao.queryAccountInfo(consNo, orgCode);
            mapList.add(accountInfoList);
            log.info("********银行信息查询完成********");
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("银行信息查询失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("******银行信息查询失败******");
            log.info(e.getMessage());
            return "1007";
        }
        try {
            //查询联系信息
            List<Map<String, Object>> contact = newApiDao.queryMobile(consNo, orgCode);
            mapList.add(contact);
            log.info("*********联系信息查询完成******");
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("联系信息查询失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("******联系信息查询失败******");
            log.info(e.getMessage());
            return "1010";
        }
        try {
            //证件信息
            List<Map<String, Object>> certificateList = newApiDao.getCertificateInfo(consNo);
            mapList.add(certificateList);
            log.info("*********证件信息查询完成********");
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("证件信息查询失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("******证件信息查询失败******");
            log.info(e.getMessage());
            return "1008";
        }
        try {
            jwLoggingEventEntity.setReturnParams(gson.toJson(mapList));
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
        } catch (Exception e) {
            log.info("********日志存储失败*****");
        }
        Map<String, Object> map1 = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        map1.put("data", mapList);
        map1.put("code", "0");
        return objectMapper.writeValueAsString(map1);
    }

    //3.4	查询电量信息
    @Override
    public String queryElectc(String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setOperation("查询电量信息");
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        //证书的UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("json验证失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****进参****json验证失败");
            return "1001";
        }
        //用户编号
        String consNo = (String) map.get("CONS_NO");
        //供电所编号
        String orgCode = (String) map.get("ORG_CODE");
        //当前时间戳
        String timeStamp = (String) map.get("TIME_STAMP");
        //请求方标识
        String appid = (String) map.get("APPID");
        //证书文件标识
        String digitCertNo = (String) map.get("DIGIT_CERT_NO");
        //日期区间(开始时间)
        String startYm = (String) map.get("START_YM");
        //日期区间(结束时间)
        String endYm = (String) map.get("END_YM");
        jwLoggingEventEntity.setUserId(appid);
        jwLoggingEventEntity.setParams("CONS_NO:" + consNo + ",ORG_CODE:" + orgCode + ",TIME_STAMP:" + timeStamp + ",APPID:" + appid + ",DIGIT_CERT_NO:" + digitCertNo +
                ",START_YM:" + startYm + ",END_YM:" + endYm);
        if (StringUtils.isBlank(consNo) || StringUtils.isBlank(orgCode) || StringUtils.isBlank(timeStamp) || StringUtils.isBlank(appid) || StringUtils.isBlank(digitCertNo) || StringUtils.isBlank(startYm) || StringUtils.isBlank(endYm)) {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        if (StringUtils.isNotBlank(digitCertNo)) {
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.eq("digit_cert_no", digitCertNo);
            JwDigitCertEntity jwDigitCertEntity = jwDigitCertService.selectOn(entityWrapper);
            if (jwDigitCertEntity != null) {
                long s = ((new Date().getTime() - jwDigitCertEntity.getUpdateTime().getTime()) / (1000 * 60 * 60));
                if (s > 24) {
                    try {
                        jwLoggingEventEntity.setReturnParams("证书失效");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    } catch (Exception e) {
                        log.info("******日志存储失败*******");
                    }
                    return "1016";
                }
                jwDigitCertEntity.setUpdateTime(new Date());
                jwDigitCertService.newUpdata(jwDigitCertEntity);
                jwLoggingEventEntity.setDigitId(jwDigitCertEntity.getDigitId());
                jwLoggingEventEntity.setIsDigit(0);
            } else {
                try {
                    jwLoggingEventEntity.setReturnParams("证书无效");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("******日志存储失败*******");
                }
                return "1015";
            }
        }else {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        try {
            List<Map<String, Object>> elecQueryList = newApiDao.queryElecQuery(consNo, orgCode, startYm, endYm);
            Map<String, Object> map1 = new HashMap<String, Object>();
            ObjectMapper objectMapper = new ObjectMapper();
            map1.put("data", elecQueryList);
            map1.put("code", "0");
            try {
                jwLoggingEventEntity.setReturnParams(gson.toJson(elecQueryList));
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*****日志储存失败*******");
            }
            return objectMapper.writeValueAsString(map1);
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("查询电量信息失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            return "1009";
        }
    }

    @Override
    public String queryPay(String json) throws JsonProcessingException {
        JwLoggingEventEntity jwLoggingEventEntity = new JwLoggingEventEntity();
        jwLoggingEventEntity.setOperation("查询缴费信息");
        jwLoggingEventEntity.setCreateDate(DateUtils.getStringDate());
        //证书的UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("json验证失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info("****进参****json验证失败");
            return "1001";
        }
        //用户编号
        String consNo = (String) map.get("CONS_NO");
        //供电所编号
        String orgCode = (String) map.get("ORG_CODE");
        //当前时间戳
        String timeStamp = (String) map.get("TIME_STAMP");
        //请求方标识
        String appid = (String) map.get("APPID");
        //证书文件标识
        String digitCertNo = (String) map.get("DIGIT_CERT_NO");
        //日期区间(开始时间)
        String startYm = (String) map.get("START_YM");
        //日期区间(结束时间)
        String endYm = (String) map.get("END_YM");
        jwLoggingEventEntity.setUserId(appid);
        jwLoggingEventEntity.setParams("CONS_NO:" + consNo + ",ORG_CODE:" + orgCode + ",TIME_STAMP:" + timeStamp + ",APPID:" + appid + ",DIGIT_CERT_NO:" + digitCertNo +
                ",START_YM:" + startYm + ",END_YM:" + endYm);
        if (StringUtils.isBlank(consNo) || StringUtils.isBlank(orgCode) || StringUtils.isBlank(timeStamp) || StringUtils.isBlank(appid) || StringUtils.isBlank(digitCertNo) || StringUtils.isBlank(startYm) || StringUtils.isBlank(endYm)) {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        if (StringUtils.isNotBlank(digitCertNo)) {
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.eq("digit_cert_no", digitCertNo);
            JwDigitCertEntity jwDigitCertEntity = jwDigitCertService.selectOn(entityWrapper);
            if (jwDigitCertEntity != null) {
                long s = ((new Date().getTime() - jwDigitCertEntity.getUpdateTime().getTime()) / (1000 * 60 * 60));
                if (s > 24) {
                    try {
                        jwLoggingEventEntity.setReturnParams("证书失效");
                        jwLoggingEventService.insertInto(jwLoggingEventEntity);
                    } catch (Exception e) {
                        log.info("*******日志存储失败****");
                    }
                    return "1016";
                }
                jwDigitCertEntity.setUpdateTime(new Date());
                jwDigitCertService.newUpdata(jwDigitCertEntity);
                jwLoggingEventEntity.setDigitId(jwDigitCertEntity.getDigitId());
                jwLoggingEventEntity.setIsDigit(0);
            } else {
                try {
                    jwLoggingEventEntity.setReturnParams("证书无效");
                    jwLoggingEventService.insertInto(jwLoggingEventEntity);
                } catch (Exception e) {
                    log.info("*******日志存储失败****");
                }
                return "1015";
            }
        }else {
            try {
                jwLoggingEventEntity.setReturnParams("参数错误");
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*******");
            }
            //参数错误
            return "1004";
        }
        try {
            List<Map<String, Object>> paymentRecs = newApiDao.queryPaymentRec(consNo, orgCode, startYm, endYm);
            for (int i = 0; i < paymentRecs.size(); i++) {
                log.info(paymentRecs.get(i).toString());
            }
            Map<String, Object> map1 = new HashMap<String, Object>();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                jwLoggingEventEntity.setReturnParams(gson.toJson(paymentRecs));
                jwLoggingEventService.insertInto(jwLoggingEventEntity);
            } catch (Exception e) {
                log.info("*******日志存储失败*****");
            }
            map1.put("data", paymentRecs);
            map1.put("code", "0");
            log.info(objectMapper.writeValueAsString(map1));
            return objectMapper.writeValueAsString(map1);
        } catch (Exception e) {
            jwLoggingEventEntity.setReturnParams("查询缴费信息失败");
            jwLoggingEventService.insertInto(jwLoggingEventEntity);
            log.info(e.getMessage());
            return "1011";
        }
    }

    //3.6	查询每日电量信息
    @Override
    public String queryDaily(String json) throws JsonProcessingException {
        //证书的UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        // LoggingEventEntity loggingEvent = new LoggingEventEntity();
        //loggingEvent.setParams(json);
        try {
            map = gson.fromJson(json, map.getClass());
        } catch (Exception e) {
            /*loggingEvent.setReturn_params("json验证失败");
            loggingEventService.save(loggingEvent);*/
            log.info("****进参****json验证失败");
            return "1001";
        }
        //用户编号
        String consNo = (String) map.get("CONS_NO");
        //供电所编号
        String orgCode = (String) map.get("ORG_CODE");
        //当前时间戳
        String timeStamp = (String) map.get("TIME_STAMP");
        //请求方标识
        String appid = (String) map.get("APPID");
        //证书文件标识
        String digitCertNo = (String) map.get("DIGIT_CERT_NO");
        //日期区间(开始时间)
        String startYm = (String) map.get("START_YM");
        //日期区间(结束时间)
        String endYm = (String) map.get("END_YM");
        if ("".equals(consNo) || "".equals(orgCode) || "".equals(timeStamp) || "".equals(appid) || "".equals(digitCertNo) || "".equals(startYm) || "".equals(endYm)) {
            //参数错误
            return "1004";
        }
        if (!"".equals(digitCertNo)) {
            EntityWrapper entityWrapper = new EntityWrapper();
            entityWrapper.eq("digit_cert_no", digitCertNo);
            JwDigitCertEntity jwDigitCertEntity = jwDigitCertService.selectOn(entityWrapper);
            if (jwDigitCertEntity != null) {
                long l = new Date().getTime() - jwDigitCertEntity.getUpdateTime().getTime();
                long day = l / (24 * 60 * 60 * 1000);
                if ((l / (60 * 60 * 1000) - day * 24) > 24) {
                    return "1016";
                }
                jwDigitCertEntity.setUpdateTime(new Date());
                jwDigitCertService.newUpdata(jwDigitCertEntity);
            } else {
                return "1015";
            }
        }
        List<DailyElectricity> dailyElectricities;
        try {
            dailyElectricities = newApiDao.queryDailyElectricity(consNo, orgCode, startYm, endYm);
        } catch (Exception e) {
            return "1017";
        }
        Map<String, Object> map1 = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        map1.put("data", dailyElectricities);
        map1.put("code", "0");
        return objectMapper.writeValueAsString(map1);
    }
}
