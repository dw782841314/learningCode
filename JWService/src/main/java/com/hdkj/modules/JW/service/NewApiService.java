package com.hdkj.modules.JW.service;

import com.baomidou.mybatisplus.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface NewApiService {
    String queryUser(String json) throws JsonProcessingException;

    String queryUserBankCertPro(String json) throws JsonProcessingException;

    String queryElectc(String json) throws JsonProcessingException;

    String queryPay(String json) throws JsonProcessingException;

    String queryDaily(String json) throws JsonProcessingException;
}
