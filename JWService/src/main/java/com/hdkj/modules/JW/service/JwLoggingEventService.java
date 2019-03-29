package com.hdkj.modules.JW.service;

import com.baomidou.mybatisplus.service.IService;
import com.hdkj.common.utils.PageUtils;
import com.hdkj.modules.JW.entity.JwLoggingEventEntity;

import java.util.Map;

/**
 * ${comments}
 *
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:18
 */
public interface JwLoggingEventService extends IService<JwLoggingEventEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insertInto(JwLoggingEventEntity JwLoggingEventEntity);
}

