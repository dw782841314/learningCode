package com.hdkj.modules.JW.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.hdkj.common.utils.PageUtils;
import com.hdkj.datasources.annotation.DataSource;
import com.hdkj.modules.JW.entity.JwDigitCertEntity;

import java.util.List;
import java.util.Map;

/**
 * 证书标识表
 *
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:19
 */
public interface JwDigitCertService extends IService<JwDigitCertEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insertInto(JwDigitCertEntity jwDigitCertEntity);

    void newUpdata(JwDigitCertEntity jwDigitCertEntity);

    JwDigitCertEntity selectOn(EntityWrapper entityWrapper);

    void deleteCert(List<String> ids);
}

