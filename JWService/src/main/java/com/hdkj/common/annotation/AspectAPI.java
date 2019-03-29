package com.hdkj.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hdkj.common.utils.AesUtil;
import com.hdkj.common.utils.Constant;
import com.hdkj.common.utils.SHA1;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AspectAPI {
    private static Logger log = LoggerFactory.getLogger(AspectAPI.class);
    private static final String SHA1KAY = "192006250b4c09247ec02edce69f6a2d";
    private static final String AESKAY = "97dc96faa1ed6a25";

    /* @Autowired
     private LoggingEventService loggingEventService;*/
    /*@Resource(name = "loggingEventService")
    private LoggingEventService loggingEventService;*/

    @Pointcut("execution(public * com.hdkj.modules.JW.controller.*.*(..)) && @annotation(com.hdkj.common.annotation.Decode)")
    public void addDecode() {
    }

    @Pointcut("execution(public * com.hdkj.modules.JW.controller.*.*(..)) && @annotation(com.hdkj.common.annotation.Encryption)")
    public void addEncryption() {
    }

    @Around(value = "addDecode()")
    public Object Signature(ProceedingJoinPoint pjp) throws Throwable {
        Gson gson = new Gson();
        Object[] args = pjp.getArgs();
        System.out.println("进入前置操作");
        Map<String, Object> msg = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        /*LoggingEventEntity loggingEvent = new LoggingEventEntity();
        loggingEvent.setOperation("QueryWithSign:加签名，不加密");*/
        try {
            if (args != null && args[0] instanceof String) {
                String parameter = (String) args[0];
                log.info("****实际进参****" + parameter);
                String signature1 = "";
                String signature2 = "";
                //截取签名
                try {
                    signature1 = parameter.substring(0, 40);
                    log.info("****进参****" + signature1);
                } catch (Exception e) {
                    log.info("****错误信息****" + e.getMessage());
                    log.info("****失败出参****1002:签名截取失败");
                    msg.put("code", "1002");
                    msg.put("msg", Constant.getCode().get("1002"));
                    /*loggingEvent.setReturn_params("签名截取失败");
                    loggingEventService.save(loggingEvent);*/
                    return objectMapper.writeValueAsString(msg);
                }
                //截取json
                parameter = parameter.substring(40);
                //生成签名
                signature2 = SHA1.getMySignByKey(parameter, SHA1KAY);
                //签名验证
                System.out.println("======signature1=====" + signature1);
                System.out.println("======signature2=====" + signature2);
                if (!signature1.equals(signature2)) {
                    //签名验证错误
                    log.info("****失败出参****1005:签名验证失败");
                    msg.put("code", "1005");
                    msg.put("msg", Constant.getCode().get("1005"));
                    /*loggingEvent.setReturn_params("签名验证失败");
                     loggingEventService.save(loggingEvent);*/
                    return objectMapper.writeValueAsString(msg);
                }
                args[0] = parameter;
            }

            Object retVal = pjp.proceed(args);
            if (Constant.getCode().containsKey(retVal)) {
                log.info("****失败出参****" + retVal);
                msg.put("code", retVal);
                msg.put("msg", Constant.getCode().get(retVal));
                /*loggingEvent.setReturn_params("未知异常");
                loggingEventService.save(loggingEvent);*/
                return objectMapper.writeValueAsString(msg);
            }
            System.out.println("进入后置操作");
            Map<String, Object> map = new HashMap<String, Object>();
            ObjectMapper json = new ObjectMapper();
            map = gson.fromJson((String) retVal, map.getClass());
            //生成签名
            String signature = SHA1.getMySignByKey(json.writeValueAsString(map), SHA1KAY);
            retVal = signature + retVal;
            log.info("******签名******"+signature);
            log.info("****实际出参****" + retVal);
            return retVal;
        } catch (Exception e) {
            System.out.println(e);
            log.info("****错误信息****" + e.getMessage());
            log.info("****失败出参****1：未知异常");
            msg.put("code", "1");
            msg.put("msg", Constant.getCode().get("1"));
            /*loggingEvent.setReturn_params("未知异常");
            loggingEventService.save(loggingEvent);*/
            return objectMapper.writeValueAsString(msg);
        }
    }

    @Around("addEncryption()")
    public Object Encryption(ProceedingJoinPoint pjp) throws Throwable {
        Map<String, Object> msg = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        Gson gson = new Gson();
        Object[] args = pjp.getArgs();
       /* LoggingEventEntity loggingEvent = new LoggingEventEntity();
        loggingEvent.setOperation("QueryWithEncrpt:加签名，加密");*/
        System.out.println("进入前置操作");
        try {
            if (args != null && args[0] instanceof String) {
                String parameter = (String) args[0];
                //loggingEvent.setParams(parameter);
                log.info("****实际进参****" + parameter);
                String signature1 = "";
                String signature2 = "";
                String json = "";
                //解密
                try {
                    json = AesUtil.aesDecrypt(parameter, AESKAY);
                    //loggingEvent.setParams(json);
                    log.info("****进参****" + json);
                } catch (Exception e) {
                    System.out.println(e);
                    log.info("****错误信息****" + e.getMessage());
                    log.info("****失败出参****1003：解密失败");
                    msg.put("code", "1003");
                    msg.put("msg", Constant.getCode().get("1003"));
                    //return objectMapper.writeValueAsString(msg);
                   /* loggingEvent.setReturn_params("解密失败");
                    loggingEventService.save(loggingEvent);*/
                    return AesUtil.aesEncrypt(objectMapper.writeValueAsString(msg), AESKAY);
                }
                //截取签名
                try {
                    signature1 = json.substring(0, 40);
                } catch (Exception e) {
                    System.out.println(e);
                    log.info("****错误信息****" + e.getMessage());
                    log.info("****失败出参****1002：签名截取失败");
                    msg.put("code", "1002");
                    msg.put("msg", Constant.getCode().get("1002"));
                   /* loggingEvent.setReturn_params("签名截取失败");
                    loggingEventService.save(loggingEvent);*/
                    return AesUtil.aesEncrypt(objectMapper.writeValueAsString(msg), AESKAY);
                }
                //截取json
                parameter = json.substring(40);
                //生成签名
                signature2 = SHA1.getMySignByKey(parameter, SHA1KAY);
                //签名验证
                System.out.println("======signature1=====" + signature1);
                System.out.println("======signature2=====" + signature2);
                if (!signature1.equals(signature2)) {
                    //签名验证错误
                    log.info("****失败出参****1005：签名验证失败");
                    msg.put("code", "1005");
                    msg.put("msg", Constant.getCode().get("1005"));
                    /*loggingEvent.setReturn_params("签名验证失败");
                    loggingEventService.save(loggingEvent);*/
                    return AesUtil.aesEncrypt(objectMapper.writeValueAsString(msg), AESKAY);
                }
                args[0] = parameter;
            }
            Object retVal = pjp.proceed(args);
            if (Constant.getCode().containsKey(retVal)) {
                log.info("****失败出参****：" + retVal);
                msg.put("code", retVal);
                msg.put("msg", Constant.getCode().get(retVal));
               /* loggingEvent.setReturn_params("未知异常");
                loggingEventService.save(loggingEvent);*/
                return AesUtil.aesEncrypt(objectMapper.writeValueAsString(msg), AESKAY);
            }
            System.out.println("进入后置操作");
            Map<String, Object> map = new HashMap<String, Object>();
            ObjectMapper json = new ObjectMapper();
            map = gson.fromJson((String) retVal, map.getClass());
            log.info("****实际出参****：" + json.writeValueAsString(map));
            //生成签名
            String signature = SHA1.getMySignByKey(json.writeValueAsString(map), SHA1KAY);
            retVal = signature + retVal;
            log.info("******签名****"+retVal);
            return AesUtil.aesEncrypt((String) retVal, AESKAY);
        } catch (Exception e) {
            System.out.println(e);
            log.info("****错误信息****" + e.getMessage());
            log.info("****失败出参****1：未知异常");
            msg.put("code", "1");
            msg.put("msg", Constant.getCode().get("1"));
           /* loggingEvent.setReturn_params("未知异常");
            loggingEventService.save(loggingEvent);*/
            return AesUtil.aesEncrypt(objectMapper.writeValueAsString(msg), AESKAY);
        }
    }
}
