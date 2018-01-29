package com.hebabr.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbGroupTypeDtoMapper;
import com.hebabr.base.service.IGroupTypeService;
import com.hebabr.model.TbGroupTypeDto;
@Service
public class GroupTypeServiceImpl implements IGroupTypeService{
	@Autowired
	private TbGroupTypeDtoMapper groupTypeMapper; 
	@Override
	public void insertGroupType(TbGroupTypeDto groupType) {
		groupTypeMapper.insert(groupType);
	}
	@Override
	public void dropGroupTypeById(String id) {
		groupTypeMapper.deleteByPrimaryKey(id);
	}
	@Override
	public TbGroupTypeDto getGroupTypeById(String id) {
		
		return groupTypeMapper.selectByPrimaryKey(id);
	}
	@Override
	public void updateGroupType(TbGroupTypeDto groupType) {
		groupTypeMapper.updateByPrimaryKey(groupType);
	}

}
