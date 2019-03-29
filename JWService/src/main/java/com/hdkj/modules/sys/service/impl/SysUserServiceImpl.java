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

package com.hdkj.modules.sys.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hdkj.common.annotation.DataFilter;
import com.hdkj.common.utils.Constant;
import com.hdkj.common.utils.PageUtils;
import com.hdkj.common.utils.Query;
import com.hdkj.modules.sys.dao.SysUserDao;
import com.hdkj.modules.sys.entity.SysDeptEntity;
import com.hdkj.modules.sys.entity.SysUserEntity;
import com.hdkj.modules.sys.service.SysDeptService;
import com.hdkj.modules.sys.service.SysUserRoleService;
import com.hdkj.modules.sys.service.SysUserService;
import com.hdkj.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public PageUtils customQuery(Map<String, Object> params) {
		String username = (String)params.get("username");
		// System.out.println(params);
		// System.out.println("username:"+username);
		Page<SysUserEntity> page = new Query<SysUserEntity>(params).getPage();		
		page.setRecords(sysUserDao.customQuery(
			page, 
			new EntityWrapper<SysUserEntity>()
				//.where("1=1", params)
				.like(StringUtils.isNotBlank(username),"username", username)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
				.orderBy("USER_ID", true)
		));

		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllMenuId(String userId) {
		return baseMapper.queryAllMenuId(userId);
	}
	
	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");
		// System.out.println(params);
		// System.out.println("username:"+username);
		Page<SysUserEntity> page = this.selectPage(
			new Query<SysUserEntity>(params).getPage(),
			new EntityWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		for(SysUserEntity sysUserEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
		}

		return new PageUtils(page);
	}

	@Override
	@DataFilter(tableAlias = "u", user = false)
	public List<SysUserEntity> queryList(Map<String, Object> params) {
		//String username = (String)params.get("username");
		//Query query = new Query(params);
		/*Page<SysUserEntity> page = this.selectPage(
			new Query<SysUserEntity>(params).getPage(),
			new EntityWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		for(SysUserEntity sysUserEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
		}*/

		/*if(!"".equals(username)){
			query.put("username",username);
		}*/

		//int total = sysUserDao.queryUserTotal(query);
		//System.out.println("******异常*******"+query.getPage());
		//PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage().getSize());


		int limit = Integer.parseInt(params.get("limit").toString());
		int page = Integer.parseInt(params.get("page").toString());
		params.put("paging",limit*page);

		List<SysUserEntity> userList = sysUserDao.queryUser(params);


		return userList;
	}

	@Override
	@DataFilter(tableAlias = "u", user = false)
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryUserTotal(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		this.insert(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			SysUserEntity userEntity = this.selectById(user.getUserId());
			user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
		}
		this.updateById(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}


	@Override
	public boolean updatePassword(String userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

	@Override
	public int updateStatus(String username) {
		// TODO Auto-generated method stub
		return sysUserDao.updateStatus(username);
	}

}
