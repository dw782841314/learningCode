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

package com.hdkj.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hdkj.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysUserDao extends BaseMapper<SysUserEntity> {

	/**
     * <p>
     * 根据 entity 条件，定制化查询用户记录（并翻页）
	 * Service层自定义传入查询条件与排序字段
     * </p>
     *
     * @param rowBounds 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param wrapper   实体对象封装操作类（可以为 null）
     * @return List<T>
     */
	@Select("SELECT ur.*,de.NAME as deptName FROM jw_user ur LEFT JOIN jw_dept de ON ur.DEPT_ID = de.DEPT_ID where 1=1 ${ew.sqlSegment}" )
    List<SysUserEntity> customQuery(RowBounds rowBounds, @Param("ew") Wrapper<SysUserEntity> wrapper);
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<String> queryAllMenuId(String userId);

	//查询用户
	List<SysUserEntity> queryUser(Map<String,Object> map);

	//查询用户分页
	int queryUserTotal(Map<String,Object> map);

	/**
	 * 用户密码错误次数过多修改状态
	 * **/
	int updateStatus(String username);

}
