package com.hebabr.base.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.pojo.ProcessDefinitionTempPojo;
import com.hebabr.base.dao.FlowProcessdefinitionDtoMapper;
import com.hebabr.base.service.IProcessDefintionService;
import com.hebabr.model.FlowProcessdefinitionDto;

@Service
public class ProcessDefintionServiceImpl implements IProcessDefintionService{

	
	@Autowired
	private FlowProcessdefinitionDtoMapper flowProcessdefintionDtoMapper;
	
	/**
	 * 
	 * 查询所有的流程定义实例
	 */
	@Override
	public List<ProcessDefinitionTempPojo> findAllProcessDefinitions() {
		List<ProcessDefinitionTempPojo> listProcessDefinitions = flowProcessdefintionDtoMapper.selectAllProcessDefinitions();
		return listProcessDefinitions ;
	}
	
	/**
	 * 
	 * 查询所有的最高版本的流程定义
	 */
	@Override
	public List<FlowProcessdefinitionDto> findAllMaxVersionProcessDefinitions() {
		List<FlowProcessdefinitionDto> listProcesses = new ArrayList<FlowProcessdefinitionDto>();
		//List<FlowProcessdefinitionDto> pds = this.findAllProcessDefinitions();
		List<FlowProcessdefinitionDto> pds = null;
		Map<String, Object> pd = new HashMap<String, Object>();
		for(FlowProcessdefinitionDto p:pds){
			pd.put(p.getProcessdefinitionKey(), p.getDevelopmentId());
		}
		Collection<Object> pdc = pd.values();
		for(Object p:pdc){
			listProcesses.add(flowProcessdefintionDtoMapper.selectByDevelopmentId((String)p));
		}
		return listProcesses;
	}
	/**
	 * 
	 * 插入流程定义的信息
	 */
	@Override
	public boolean insert(FlowProcessdefinitionDto flowProcessdefinitionDto) {
		
	 	boolean flag = true;
		int result = flowProcessdefintionDtoMapper.insert(flowProcessdefinitionDto);
		if(result==0)
			flag = false;
		return flag;
	}

	/**
	 * 
	 * 更新流程定义的信息
	 */
	@Override
	public boolean updateFlowProcessdefinitionDto(FlowProcessdefinitionDto flowProcessdefinitionDto) {
		
		boolean flag = true;
		int result = flowProcessdefintionDtoMapper.updateByPrimaryKey(flowProcessdefinitionDto);
		if(result==0)
			flag = false;
		return flag;
	}

	/**
	 * 
	 * 查找
	 */
	@Override
	public FlowProcessdefinitionDto findFlowProcessdefinitionById(String id) {
		FlowProcessdefinitionDto  flowProcessdefinitionDto = flowProcessdefintionDtoMapper.selectByPrimaryKey(id);
		return flowProcessdefinitionDto;
	}

	/**
	 * 
	 * 查询通过流程定义ID发布的流程定义
	 */
	@Override
	public FlowProcessdefinitionDto selectProcessdefinitionByDevelopmentId(String developmentid) {
		FlowProcessdefinitionDto flowProcessdefinitionDto = flowProcessdefintionDtoMapper.selectByDevelopmentId(developmentid);
		return flowProcessdefinitionDto;
	}

	/**
	 * 
	 * 通过组织机构Id获取所有的可用的流程定义模板
	 */
	@Override
	public List<FlowProcessdefinitionDto> selectAllCurrentUserProcessDefintionsByOrgId(String orgId) {
		
		List<FlowProcessdefinitionDto> listProcessDefinitions = flowProcessdefintionDtoMapper.selectAllCurrentUserProcessDefintionsByOrgId(orgId);
				
		return listProcessDefinitions;
	}

	/**
	 * 查询所有的启用的流程定义
	 */
	@Override
	public List<FlowProcessdefinitionDto> findAllIsUsedUserProcessDefintions() {
		List<FlowProcessdefinitionDto> listPds = flowProcessdefintionDtoMapper.selectAllIsUsedUserProcessDefintions();
		return listPds;
	}

	@Override
	public List<FlowProcessdefinitionDto> findByQualTypeId(String qualTypeId) {
		List<FlowProcessdefinitionDto> listProcessDefinition = flowProcessdefintionDtoMapper.selectByQualTypeId(qualTypeId);
		return listProcessDefinition;
	}
	
	@Override
	public List<FlowProcessdefinitionDto> findByQualTypeIdMap(Map<String,String> map) {
		List<FlowProcessdefinitionDto> listProcessDefinition = flowProcessdefintionDtoMapper.selectByQualTypeIdMap(map);
		return listProcessDefinition;
	}

	// 根据id来更新状态
	@Override
	public void updateDefintionById(String id) {
		flowProcessdefintionDtoMapper.updateDefintionById(id);
		
	}
 
	//搜索流程模板
	@Override
	public List<ProcessDefinitionTempPojo> selectProcessDefinitions(Map map) {
		
		return flowProcessdefintionDtoMapper.selectProcessDefinitions(map);
	}

	@Override
	public List<FlowProcessdefinitionDto> findByQualTypeIdAndNotIsProjectManager(Map<String,String> map) {
		return flowProcessdefintionDtoMapper.selectByQualTypeIdAndNotIsProjectManager(map);
	}
}
