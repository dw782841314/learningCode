package com.hdkj.modules.JW.service.impl;

import com.hdkj.common.utils.PageUtils;
import com.hdkj.common.utils.Query;
import com.hdkj.datasources.annotation.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.hdkj.modules.JW.dao.JwDigitCertDao;
import com.hdkj.modules.JW.entity.JwDigitCertEntity;
import com.hdkj.modules.JW.service.JwDigitCertService;


@Service("jwDigitCertService")
public class JwDigitCertServiceImpl extends ServiceImpl<JwDigitCertDao, JwDigitCertEntity> implements JwDigitCertService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String digitId = (String) params.get("digitId");
        String digitCertNo = (String) params.get("digitCertNo");
        Page<JwDigitCertEntity> page = this.selectPage(
                new Query<JwDigitCertEntity>(params).getPage(),
                new EntityWrapper<JwDigitCertEntity>()
                .like(StringUtils.isNotBlank(digitId), "DIGIT_ID", digitId)
                .like(StringUtils.isNotBlank(digitId), "DIGIT_CERT_NO", digitCertNo)
                .orderBy("UPDATE_TIME",false)
        );

        return new PageUtils(page);
    }

    @Override
    @DataSource(name = "first")
    public void insertInto(JwDigitCertEntity jwDigitCertEntity) {
        this.insert(jwDigitCertEntity);
    }

    @Override
    @DataSource(name = "first")
    public void newUpdata(JwDigitCertEntity jwDigitCertEntity) {
        this.updateById(jwDigitCertEntity);
    }

    @Override
    @DataSource(name = "first")
    public JwDigitCertEntity selectOn(EntityWrapper entityWrapper) {
        return this.selectOne(entityWrapper);
    }

    @Override
    @DataSource(name = "first")
    public void deleteCert(List<String> ids) {
        this.deleteBatchIds(ids);
    }

}
