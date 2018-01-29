package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbGroupDtoMapper;
import com.hebabr.base.dao.TbGroupTypeDtoMapper;
import com.hebabr.base.dao.TbUserVsGroupDtoMapper;
import com.hebabr.base.pojo.UserVsGroupPojo;
import com.hebabr.base.service.IGroupService;
import com.hebabr.model.TbGroupDto;
import com.hebabr.model.TbGroupDtoExample;
import com.hebabr.model.TbGroupTypeDto;
import com.hebabr.model.TbGroupTypeDtoExample;
import com.hebabr.model.TbUserVsGroupDto;
import com.hebabr.model.TbUserVsGroupDtoExample;

@Service
public class GroupServiceImpl implements IGroupService {

	@Autowired
    private TbGroupDtoMapper groupMapper; 
	@Autowired
	private TbGroupTypeDtoMapper groupTypeMapper;
	@Autowired
	private TbUserVsGroupDtoMapper userVsGroupMapper;
	
	@Override
	public int insertGroup(TbGroupDto group) {
		// TODO Auto-generated method stub
		return groupMapper.insert(group);
	}

	@Override
	public List<TbGroupDto> getAllGroups() {
		// TODO Auto-generated method stub
		TbGroupDtoExample tgde = new TbGroupDtoExample();
		tgde.createCriteria().andIsUsedEqualTo(true);
		return groupMapper.selectByExample(tgde);
	}

	@Override
	public List<TbGroupTypeDto> getAllGroupTypesUsed() {
		TbGroupTypeDtoExample tgde = new TbGroupTypeDtoExample();
		tgde.createCriteria().andIsUsedEqualTo(true);
		return groupTypeMapper.selectByExample(tgde);
	}

	@Override
	public List<TbGroupDto> getGroupsByTypeId(String typeId) {
		TbGroupDtoExample tgde = new TbGroupDtoExample();
		tgde.createCriteria().andIsUsedEqualTo(true).andGroupTypeIdEqualTo(typeId);
		return groupMapper.selectByExample(tgde);
	}
	
	@Override
	public List<TbGroupDto> getAllGroupsByTypeId(String typeId) {
		TbGroupDtoExample tgde = new TbGroupDtoExample();
		tgde.createCriteria().andGroupTypeIdEqualTo(typeId);
		return groupMapper.selectByExample(tgde);
	}

	@Override
	public TbGroupDto getGroupById(String id) {
		return groupMapper.selectByPrimaryKey(id);
	}

	@Override
	public String getGroupTypeIdByName(String groupTypeName) {
		return groupTypeMapper.getGroupTypeIdByName(groupTypeName);
	}
	@Override
	public void dropGroupById(String id) {
		groupMapper.deleteByPrimaryKey(id);
			
	}

	@Override
	public void updateGroup(TbGroupDto group) {
		groupMapper.updateByPrimaryKey(group);
		
	}

	@Override
	public int insertUserVsGroup(TbUserVsGroupDto ug) {
		// TODO Auto-generated method stub
		return userVsGroupMapper.insert(ug);
	}
	
	@Override
	public void dropUserVsGroupByUserId(String userId) {
		// TODO Auto-generated method stub
		userVsGroupMapper.deleteUserVsGroupByUserId(userId);
	}
	
	@Override
	public List<String> listGroupIdsByUserId(String userId) {
		return userVsGroupMapper.getGroupIdsByUserId(userId);
	}
	
	@Override
	public List<TbUserVsGroupDto> listUserVsGroupsByGroupId(String groupId) {
		
		TbUserVsGroupDtoExample tude = new TbUserVsGroupDtoExample();
		com.hebabr.model.TbUserVsGroupDtoExample.Criteria cc=tude.createCriteria();
		cc.andGroupIdEqualTo(groupId).andIsUsedEqualTo(true);
		return userVsGroupMapper.selectByExample(tude);
	}
	
	@Override
	public List<String> listUserIdsByGroupId(String groupId) {
		return userVsGroupMapper.getUserIdsByGroupId(groupId);
	}
	
	@Override
	public void dropUserVsGroupByGroupId(String groupId) {
		userVsGroupMapper.deleteUserVsGroupByGroupId(groupId);
	}
	
	@Override
	public void dropUserVsGroupByUserIdAndGroupId(String userId, String groupId) {
		userVsGroupMapper.deleteUserVsGroupByUserIdAndGroupId(userId, groupId);
	}

	@Override
	public void deleteGroupByGroupTypeId(String groupTypeId) {
		groupMapper.deleteGroupByGroupTypeId(groupTypeId);
			
	}

	@Override
	public void deleteUserVsGroupByGroupTypeId(String groupTypeId) {
		userVsGroupMapper.deleteUserVsGroupByGroupTypeId(groupTypeId);
		
	}

	@Override
	public void deleteUserVsGroupByGroupId(String groupId) {
		userVsGroupMapper.deleteUserVsGroupByGroupId(groupId);
	}

	@Override
	public List<TbGroupTypeDto> getAllGroupTypes() {
		TbGroupTypeDtoExample tgde = new TbGroupTypeDtoExample();
		tgde.createCriteria();
		return groupTypeMapper.selectByExample(tgde);
	}

	@Override
	public TbGroupDto getUsedGroupId(String id) {
		TbGroupDtoExample tgde = new TbGroupDtoExample();
		tgde.createCriteria().andIdEqualTo(id).andIsUsedEqualTo(true);
		List<TbGroupDto> list=groupMapper.selectByExample(tgde);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<String> getGroupNamesByUserId(String userId) {
		return groupMapper.getGroupNamesByUserId(userId);
	}

	@Override
	public List<UserVsGroupPojo> getUvGPojoSelectedByGroupId(String groupId,Boolean isChecked) {
		if(isChecked){
			return groupMapper.getUvGPojoSelectedByGroupId(groupId);
		}else{
			return groupMapper.getUvGPojoUnSelectedByGroupId(groupId);
		}
	}
}
