package com.hebabr.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbRoleDtoMapper;
import com.hebabr.base.dao.TbUserVsRoleDtoMapper;
import com.hebabr.base.service.IRoleService;
import com.hebabr.model.TbRoleDto;
import com.hebabr.model.TbRoleDtoExample;
import com.hebabr.model.TbUserVsRoleDto;

@Service
public class RoleServiceImpl implements IRoleService {
	
	@Autowired
    private TbRoleDtoMapper roleMapper; 
	@Autowired
	private TbUserVsRoleDtoMapper userVsRoleMapper;

	@Override
	public List<TbRoleDto> getAllRoles(Map map) {
		// TODO Auto-generated method stub
		/*TbRoleDtoExample trde = new TbRoleDtoExample();
		trde.createCriteria().andIsUsedEqualTo(true);*/
		return roleMapper.getAllRoles(map);
	}

	@Override
	public int saveRole(TbRoleDto role) {
		// TODO Auto-generated method stub
		return roleMapper.insert(role);
	}

	@Override
	public void dropRole(String id) {
		// TODO Auto-generated method stub
		roleMapper.deleteByPrimaryKey(id);
		
		
	}

	@Override
	public void alterRole(TbRoleDto role) {
		// TODO Auto-generated method stub
		roleMapper.updateByPrimaryKey(role);
		
	}

	@Override
	public TbRoleDto findRoleById(String id) {
		// TODO Auto-generated method stub
		return roleMapper.selectByPrimaryKey(id);
		
	}
	
	@Override
	public int insertUserVsRole(TbUserVsRoleDto ur) {
		// TODO Auto-generated method stub
		return userVsRoleMapper.insert(ur);
	}
	
	@Override
	public void dropUserVsRoleByUserId(String userId) {
		userVsRoleMapper.deleteUserVsRoleByUserId(userId);
	}
	
	@Override
	public List<String> listRoleIdsByUserId(String userId) {//待修改
		// TODO Auto-generated method stub
		return userVsRoleMapper.getRoleIdsByUserId(userId);
	}
	
	@Override
	public List<TbRoleDto> getAllRolesUsed() {
		// TODO Auto-generated method stub
		TbRoleDtoExample trde = new TbRoleDtoExample();
		trde.createCriteria().andIsUsedEqualTo(true);
		return roleMapper.selectByExample(trde);
	}

	@Override
	public List<String> getRoleNamesByUserId(String userId) {
		return roleMapper.getRoleNamesByUserId(userId);
	}
}
