package com.hebabr.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.service.IFlowProcessdefinitionService;
import com.hebabr.base.dao.TbOrgDtoMapper;
import com.hebabr.base.dao.TbOrgTypeDtoMapper;
import com.hebabr.base.dao.TbUserVsOrgDtoMapper;
import com.hebabr.base.service.IOrgService;
import com.hebabr.model.FlowProcessdefinitionDto;
import com.hebabr.model.TbOrgDto;
import com.hebabr.model.TbOrgDtoExample;
import com.hebabr.model.TbOrgDtoExample.Criteria;
import com.hebabr.model.TbOrgTypeDto;
import com.hebabr.model.TbOrgTypeDtoExample;
import com.hebabr.model.TbUserVsOrgDto;
@Service
public class OrgServiceImpl implements IOrgService {
	
	@Autowired
    private TbOrgDtoMapper orgMapper; 
	@Autowired
    private TbOrgTypeDtoMapper orgTypeMapper;
	@Autowired
	private TbUserVsOrgDtoMapper userVsOrgMapper;
	@Autowired
	private IFlowProcessdefinitionService iFlowProcessdefinitionService;
	@Override
	public List<TbOrgTypeDto> getAllOrgTypes() {
		// TODO Auto-generated method stub
		TbOrgTypeDtoExample totde = new TbOrgTypeDtoExample();
		// 注意这里的字段名，必须和数据库里的保持一致而不是bean里的属性
		totde.setOrderByClause("sort_no ASC"); 
		totde.createCriteria();
		return orgTypeMapper.selectByExample(totde);
	}
	
	public List<TbOrgDto> getAllOrgByTypeAndParentId(String orgTypeId, String parentId){
		// TODO Auto-generated method stub
		TbOrgDtoExample tode = new TbOrgDtoExample();
		tode.setOrderByClause("sort_no ASC"); 
		Criteria cc = tode.createCriteria();
		if(orgTypeId != null){
			cc.andOrgTypeIdEqualTo(orgTypeId);
		}
		if(parentId != null){
			cc.andParentIdEqualTo(parentId);
		}
		return orgMapper.selectByExample(tode);
	}

	@Override
	public TbOrgTypeDto getOrgTypeById(String orderTypeId) {
		// TODO Auto-generated method stub
		return orgTypeMapper.selectByPrimaryKey(orderTypeId);
	}

	@Override
	public TbOrgDto getOrgById(String orderId) {
		// TODO Auto-generated method stub
		return orgMapper.selectByPrimaryKey(orderId);
	}

	//获取org的最大序号
	@Override
	public int getMaxNumOfOrg(String orgTypeId, String parentId) {
		int max;
		if(orgTypeId != null){
			max =orgMapper.getMaxOfOrgSortNumByOrgTypeId(orgTypeId);
		}else{
			max=orgMapper.getMaxOfOrgSortNumByParentId(parentId);
		}
		return max;
	}

	@Override
	public int getMaxNumOfOrgType() {
		int max =orgTypeMapper.getMaxOfOrgTypeSortNum();
		return max;
	}

	@Override
	public int saveOrg(TbOrgDto org) {
		// TODO Auto-generated method stub
		return orgMapper.insert(org);
	}

	@Override
	public int saveOrgType(TbOrgTypeDto orgType) {
		// TODO Auto-generated method stub
		return orgTypeMapper.insert(orgType);
	}

	@Override
	public void dropOrgTypeById(String id) {
		// TODO Auto-generated method stub
		orgTypeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateOrgType(TbOrgTypeDto orgType) {
		orgTypeMapper.updateByPrimaryKey(orgType);
		
	}

	@Override
	public void updateOrg(TbOrgDto org) {
		// TODO Auto-generated method stub
		orgMapper.updateByPrimaryKey(org);
	}

	@Override
	public void dropOrgById(String id) {
		// TODO Auto-generated method stub
		orgMapper.deleteByPrimaryKey(id);
		
	}

	@Override
	public List<TbOrgDto> getAllOrgByTypeId(String typeId) {
		// TODO Auto-generated method stub
		TbOrgDtoExample tode = new TbOrgDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andOrgTypeIdEqualTo(typeId).andIsUsedEqualTo(true);
		return orgMapper.selectByExample(tode);
	}

	@Override
	public List<TbOrgDto> getAllOrgsByParentId(String parentId) {
		// TODO Auto-generated method stub
		TbOrgDtoExample tode = new TbOrgDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId);
		return orgMapper.selectByExample(tode);
	}

	@Override
	public TbOrgDto getOrgByName(String orgName) {
		// TODO Auto-generated method stub
		return orgMapper.getOrgByName(orgName);
	}
	@Override
	public void dropUserVsOrgById(String id) {
		// TODO Auto-generated method stub
		userVsOrgMapper.deleteByPrimaryKey(id);
		
	}
	
	@Override
	public int insertUserVsOrg(TbUserVsOrgDto uo) {
		// TODO Auto-generated method stub
		return userVsOrgMapper.insert(uo);
	}
	
	@Override
	public void dropUserVsOrgsByUserId(String userId) {
		userVsOrgMapper.deleteUserVsOrgsByUserId(userId);
	}
	
	@Override
	public List<String> listOrgIdsByUserId(String userId) {
		return userVsOrgMapper.getorgIdsByUserId(userId);
	}

	@Override
	public List<TbOrgDto> getHavingQualOrgs() {
		TbOrgDtoExample tode = new TbOrgDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andIsHaveQualEqualTo(true);
		return orgMapper.selectByExample(tode);
	}
	@Override
		public List<TbOrgDto> getAllUsedOrgs() {
		TbOrgDtoExample tode = new TbOrgDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andIsUsedEqualTo(true);
		return orgMapper.selectByExample(tode);
		}
	@Override
	public List<TbOrgDto> getHavingQualOrgs(String attrValue) {
		//通过developmentID 获取资质类别
		FlowProcessdefinitionDto flowProcessdefinitionDto=iFlowProcessdefinitionService.findFlowProcessdefinitionByDevelopmentId(attrValue);
		String qualTypeId=null;
		if(flowProcessdefinitionDto!=null){
			qualTypeId=flowProcessdefinitionDto.getQualTypeId();
		}
		List<TbOrgDto> orgs= new ArrayList<TbOrgDto>();
		if("12345678901234567890123456789011".equals(qualTypeId)){
			//TbOrgDto org=this.getOrgByName("河北建研科技有限公司");
			TbOrgDto org = this.getOrgById("bf45a9b8049711e79bac00163e0314a1");
			orgs.add(org);
		}else{
			orgs = this.listOrgsHasQual();
		
		}
		
		return orgs;
	}
	
	
	
	@Override
	public List<TbOrgTypeDto> getAllOrgTypesUsed() {
		TbOrgTypeDtoExample totde = new TbOrgTypeDtoExample();
		// 注意这里的字段名，必须和数据库里的保持一致而不是bean里的属性
		totde.setOrderByClause("sort_no ASC"); 
		totde.createCriteria().andIsUsedEqualTo(true);
		return orgTypeMapper.selectByExample(totde);
	}

	@Override
	public List<TbOrgDto> getAllUsedOrgByTypeAndParentId(String orgTypeId, String parentId) {
		TbOrgDtoExample tode = new TbOrgDtoExample();
		tode.setOrderByClause("sort_no ASC"); 
		Criteria cc = tode.createCriteria();
		cc.andIsUsedEqualTo(true);
		if(orgTypeId != null){
			cc.andOrgTypeIdEqualTo(orgTypeId);
		}
		if(parentId != null){
			cc.andParentIdEqualTo(parentId);
		}
		return orgMapper.selectByExample(tode);
	}

	@Override
	public List<TbOrgDto> getAllUsedOrgsByParentId(String parentId) {
		TbOrgDtoExample tode = new TbOrgDtoExample();
		Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId).andIsUsedEqualTo(true);
		return orgMapper.selectByExample(tode);
	}

	@Override
	public List<TbOrgDto> listOrgsHasQual() {
		
			
		return orgMapper.listOrgsHasQual();
	}

}
