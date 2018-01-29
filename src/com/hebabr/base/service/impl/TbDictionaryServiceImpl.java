package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.FlowTechPlanApprovalDtoMapper;
import com.hebabr.base.dao.TbDictionaryDtoMapper;
import com.hebabr.base.dao.TbRegionDtoMapper;
import com.hebabr.base.service.ITbDictionaryService;
import com.hebabr.model.TbDictionaryDto;
import com.hebabr.model.TbDictionaryDtoExample;
import com.hebabr.model.TbRegionDto;
import com.hebabr.model.TbRegionDtoExample;
@Service
public class TbDictionaryServiceImpl implements ITbDictionaryService{

	
	@Autowired
	private TbDictionaryDtoMapper tbDictionaryDtoMapper;
	@Autowired
	private TbRegionDtoMapper regionDtoMapper;
	/**
	 * 
	 * 查询所有的工程进度列表信息
	 */
	@Override 
	public List<TbDictionaryDto> findAllGCJDList() {
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByParantIdAsc("12345678901234567890123456789001");
		return listDiction;
	}

	/**
	 * 
	 * 查询所有的项目类别列表信息
	 */
	@Override
	public List<TbDictionaryDto> findAllXMLBList() {
		
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByParantIdAsc("12345678901234567890123456789002");
		return listDiction;
	}

	@Override
	public List<TbDictionaryDto> findAllXMLBList(String orgId) {
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByOrgId(orgId);
		return listDiction;
	}
	
	@Override
	public List<TbDictionaryDto> findAllXMLBList(String orgId,String attrValue) {
		/*if("qualborrowJYKJ_flowProcess:1:2000004".equals(attrValue)){
			orgId="root";
		}*/
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByOrgId(orgId);
		return listDiction;
	}

	@Override
	public List<TbRegionDto> getRegionByParentId(Double regionId) {
		TbRegionDtoExample tre = new TbRegionDtoExample();
		tre.createCriteria().andParentIdEqualTo(regionId);
		return regionDtoMapper.selectByExample(tre);
	}

	//@黄  获取所有的合同类型
	@Override
	public List<TbDictionaryDto> findAllHTLXList() {
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByParantIdAsc("12345678901234567890123456789037");
			return listDiction;
	}

	//@黄 获取所有的工程类型
	@Override
	public List<TbDictionaryDto> findAllGCLXList() {
		List<TbDictionaryDto> listDiction = tbDictionaryDtoMapper.selectByParantIdAsc("12345678901234567890123456789041");
			return listDiction;
	}


	@Override
	public TbDictionaryDto selectDictionaryByPrimaryKey(String primaryKey) {
		return tbDictionaryDtoMapper.selectByPrimaryKey(primaryKey);
	}
	
	
	@Override
	public List<TbDictionaryDto> selectDictionaryByParentId(String parentId) {
		TbDictionaryDtoExample tode = new TbDictionaryDtoExample();
		TbDictionaryDtoExample.Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId);
		return tbDictionaryDtoMapper.selectByExample(tode);
	}

	@Override
	public int updateDictionary(TbDictionaryDto tbDictionaryDto) {
		return tbDictionaryDtoMapper.updateByPrimaryKey(tbDictionaryDto);
	}


	@Override
	public List<TbDictionaryDto> findAllTechTypes() {
		
		return tbDictionaryDtoMapper.findAllTechTypes();
	}

	@Override
	public String getMaxTeachPlanNum(String year) {
		TbDictionaryDto d=tbDictionaryDtoMapper.selectByPrimaryKey("12345678901234567890123456789111");
		if(d!=null){
			String max=d.getDictionaryComment();
			String yearBefore=max.substring(0,4);
			if(!year.equals(yearBefore)){//年份不一致，更新数据库到当前年份
				d.setDictionaryComment(year+"0000");
				tbDictionaryDtoMapper.updateByPrimaryKey(d);
				return year+"0000";
			}else{//获取到编号增一
				String maxNum=max.substring(4,8);
				int mNum=Integer.valueOf(maxNum);
				mNum++;
				String numStr = String.format("%04d",mNum);
				d.setDictionaryComment(year+numStr);
				tbDictionaryDtoMapper.updateByPrimaryKey(d);
				return year+numStr;
			}
		}else{
			return year+"0000";
		}
	}


}
