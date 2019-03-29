package com.hdkj.modules.JW.service.impl;

import com.hdkj.common.utils.PageUtils;
import com.hdkj.common.utils.Query;
import com.hdkj.datasources.annotation.DataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.hdkj.modules.JW.dao.JwLoggingEventDao;
import com.hdkj.modules.JW.entity.JwLoggingEventEntity;
import com.hdkj.modules.JW.service.JwLoggingEventService;


@Service("jwLoggingEventService")
public class JwLoggingEventServiceImpl extends ServiceImpl<JwLoggingEventDao, JwLoggingEventEntity> implements JwLoggingEventService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String startYm = (String) params.get("startYm");
        String endYm = (String) params.get("endYm");
        Page<JwLoggingEventEntity> page = this.selectPage(
                new Query<JwLoggingEventEntity>(params).getPage(),
                new EntityWrapper<JwLoggingEventEntity>()
                .like(StringUtils.isNotBlank(userId),"USER_ID",userId)
                .between(StringUtils.isNotBlank(startYm),"create_date",startYm,endYm)
                .orderBy("CREATE_DATE",false)

        );

        return new PageUtils(page);
    }

    @Override
    @DataSource(name = "first")
    public void insertInto(JwLoggingEventEntity JwLoggingEventEntity) {
        this.insert(JwLoggingEventEntity);
    }


}
