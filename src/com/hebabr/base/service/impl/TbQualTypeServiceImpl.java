package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbQualTypeMapper;
import com.hebabr.base.service.ITbQualTypeService;
import com.hebabr.model.TbQualType;
import com.hebabr.model.TbQualTypeExample;
import com.hebabr.model.TbQualTypeExample.Criteria;
@Service
public class TbQualTypeServiceImpl implements ITbQualTypeService{

	
	@Autowired
	private TbQualTypeMapper tbQualTypeMapper;
	/**
	 * 
	 * 获取所有的资质类别的信息
	 */
	@Override
	public List<TbQualType> findAllQualType() {
		List<TbQualType> lsitTbqualType = tbQualTypeMapper.selectAllQualType();
		return lsitTbqualType;
	}
	@Override
	public List<TbQualType> findAllQualTypeUsed() {
		List<TbQualType> lsitTbqualType = tbQualTypeMapper.selectAllQualTypeUsed();
		return lsitTbqualType;
	}
	
	@Override
	public TbQualType findByProcessDefinitionId(String processDefinitionId) {
		TbQualType tbQualType = tbQualTypeMapper.selectProcessDefinitionId(processDefinitionId);
		return tbQualType;
	}
	@Override
	public TbQualType findById(String id) {
		return tbQualTypeMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<TbQualType> findQualTypesByParentId(String parentId) {
		TbQualTypeExample tode = new TbQualTypeExample();
		Criteria cc = tode.createCriteria(); 
		cc.andParentIdEqualTo(parentId).andIsUsedEqualTo(true);
		return tbQualTypeMapper.selectByExample(tode);
	}
	@Override
	public List<TbQualType> findProcessQualTypesUsed() {
		TbQualTypeExample tode = new TbQualTypeExample();
		Criteria cc = tode.createCriteria(); 
		cc.andIsUsedEqualTo(true).andParentIdIsNull();  
		return tbQualTypeMapper.selectByExample(tode);
	}

}
