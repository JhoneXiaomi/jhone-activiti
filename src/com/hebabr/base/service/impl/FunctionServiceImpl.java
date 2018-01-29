package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbFunctionDtoMapper;
import com.hebabr.base.dao.TbRoleVsFunctionDtoMapper;
import com.hebabr.base.pojo.FunctionMenuPojo;
import com.hebabr.base.pojo.RoleVsFunctionListPojo;
import com.hebabr.base.pojo.TbFunctionPojo;
import com.hebabr.base.service.IFunctionService;
import com.hebabr.base.service.IRoleService;
import com.hebabr.model.TbFunctionDto;
import com.hebabr.model.TbFunctionDtoExample;
import com.hebabr.model.TbFunctionDtoExample.Criteria;
import com.hebabr.model.TbMenuDto;
import com.hebabr.model.TbRoleVsFunctionDto;
import com.hebabr.model.TbRoleVsFunctionDtoExample;

@Service
public class FunctionServiceImpl implements IFunctionService {

	@Autowired
    private TbFunctionDtoMapper functionMapper;
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IFunctionService functionService;
	@Autowired
	private TbRoleVsFunctionDtoMapper roleVsFunctionMapper;
	@Override
	public TbFunctionDto getFunctionById(String id) {
		// TODO Auto-generated method stub
		return functionMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<TbFunctionDto> getFunctionListByUserId(String userId) {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionListByUserId(userId);
		
	}
	
	
	@Override
	public List<TbFunctionDto> getFunctionsByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return functionMapper.getFunctionsByRoleId(roleId);
	}
	@Override
	public List<TbFunctionDto> getAllFunctions() {
		// TODO Auto-generated method stub
		TbFunctionDtoExample tode = new TbFunctionDtoExample();
		Criteria cc = tode.createCriteria(); 
		cc.andIsUsedEqualTo(true);
		return functionMapper.selectByExample(tode);
	}
	@Override
	public List<TbFunctionDto> getFunctionsByParentId(String parentId) {
		// TODO Auto-generated method stub
		TbFunctionDtoExample tode = new TbFunctionDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId).andIsUsedEqualTo(true);
		return functionMapper.selectByExample(tode);
	}
	
	@Override
	public List<TbFunctionDto> getAllFunctionsByParentId(String parentId) {
		// TODO Auto-generated method stub
		TbFunctionDtoExample tode = new TbFunctionDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId);
		return functionMapper.selectByExample(tode);
	}
	
	@Override
	public List<TbRoleVsFunctionDto> getRoleVsFunctionsByRoleId(String roleId) {
		// TODO Auto-generated method stub
		TbRoleVsFunctionDtoExample tode = new TbRoleVsFunctionDtoExample();
		com.hebabr.model.TbRoleVsFunctionDtoExample.Criteria cc = tode.createCriteria();
		cc.andRoleIdEqualTo(roleId).andIsUsedEqualTo(true);
		return roleVsFunctionMapper.selectByExample(tode);
	}
	
	@Override
	public List<RoleVsFunctionListPojo> getAllRoleVsFunctionPojo(String roleId) {
		return roleVsFunctionMapper.selectAllRoleVsFunctionPojo(roleId);
	}
	
	@Override
	public void dropByRoleIdAndFunctionId(String roleId, String functionId) {
		//System.out.println("roleId "+roleId+"   fId  "+functionId);
		roleVsFunctionMapper.dropRoleVsFunctionByRoleIdAndFunctionId(roleId, functionId);
	}
	
	@Override
	public void addRoleVsFunction(TbRoleVsFunctionDto roleVsFunction) {
		// TODO Auto-generated method stub
		//System.out.println("insert");
		roleVsFunctionMapper.insert(roleVsFunction);
		
	}
	@Override
	public List<String> getFunctionIdsByUserId(String userId) {
		return functionMapper.getFunctionIdsByUserId(userId);
	}
	@Override
	public List<TbFunctionDto> getFunctionsByMenuIdAndParentId(String menuId, String parentId) {
		TbFunctionDtoExample tode = new TbFunctionDtoExample();
		com.hebabr.model.TbFunctionDtoExample.Criteria cc = tode.createCriteria();
		cc.andMenuIdEqualTo(menuId).andParentIdEqualTo(parentId).andIsUsedEqualTo(true);
		return functionMapper.selectByExample(tode);
	}
	@Override
	public void updateFunction(TbFunctionDto function) {
		functionMapper.updateByPrimaryKey(function);
	}
	@Override
	public List<FunctionMenuPojo> getFunctionMenuPojoList() {
		return functionMapper.getFunctionMenuPojoList();
	}
	@Override
	public List<TbMenuDto> getMenuListByUserId(String userId) {
		
		return functionMapper.getMenuListByUserId(userId);
	}
	@Override
	public TbFunctionPojo getFunctionPojoById(String functionId) {
		return functionMapper.getFunctionPojoById(functionId);
	}
	@Override
	public void saveTbFunctionDto(TbFunctionDto tbFunctionDto) {
		functionMapper.updateByPrimaryKey(tbFunctionDto);
	}
	@Override
	public List<TbFunctionPojo> getFunctionPojoListByParentId(String functionPId) {
		return functionMapper.getFunctionPojoListByParentId(functionPId);
	}
	@Override
	public int addFunctionDto(TbFunctionDto tbFunctionDto) {
		return functionMapper.insert(tbFunctionDto);
	}
	@Override
	public List<TbFunctionPojo> fuzzyGetFunctionPojoListByName(String fuzzyName) {
		return functionMapper.fuzzyGetFunctionPojoListByName(fuzzyName);
	}
	
}
