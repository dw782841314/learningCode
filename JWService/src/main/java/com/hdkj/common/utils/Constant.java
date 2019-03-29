/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.hdkj.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2016-11-15
 */
public class Constant {
	/** 超级管理员ID */
	public static final String SUPER_ADMIN = "1";
    /** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";


	/**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static Map<String, String> getCode(){
        Map<String, String> map= new HashMap<String, String>();
        map.put("1001", "json验证失败");
        map.put("1002", "签名截取失败");
        map.put("1003", "解密失败");
        map.put("1004", "参数不正确");
        map.put("1005", "签名验证失败");
        map.put("1006", "用户档案查询失败");
        map.put("1007", "银行账号信息查询失败");
        map.put("1008", "证件信息查询失败");
        map.put("1009", "应收电量查询失败");
        map.put("1010", "联系电话查询失败");
        map.put("1011", "缴费记录查询失败");
        map.put("1012", "用户编号或单位编号未找到");
        map.put("1013", "用户查询失败");
        map.put("1014", "用户未找到");
        map.put("1015", "证书标识无效");
        map.put("1016", "证书标识失效");
        map.put("1017", "每日电量查询失败");
        map.put("1018", "变更信息查询失败");
        map.put("1019", "证书及证书标识均为空");
        map.put("0", "成功");
        map.put("1", "未知异常");
        return map;
    }

}
