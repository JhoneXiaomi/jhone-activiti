package com.hebabr.base.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

public class ProcessUtil {

	/**
	 * 
	 * @Description 获取流程定义节点的相关信息
	 *
	 * @param repositoryService
	 *            流程定义及流程部署提供服务
	 * @param processDefinitionId
	 *            流程定义id
	 * @param activityId
	 *            流程定义节点id
	 * @return 节点虚拟机
	 */
	public static ActivityImpl getActivity(RepositoryService repositoryService, String processDefinitionId,
			String activityId) {

		ReadOnlyProcessDefinition processDefinition = ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		if (null != processDefinition) {
			ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;
			return processDefinitionEntity.findActivity(activityId);
		}

		return null;
	}

	/**
	 * 
	 * @Description 根据流程定义id获取流程定义实体
	 *
	 * @param processEngine 流程引擎
	 * @param processDefinitionId 流程定义id
	 * @return 流程定义实体
	 */
	public static ProcessDefinitionEntity getProcessDefinitionEntity(ProcessEngine processEngine,
			String processDefinitionId) {
		ReadOnlyProcessDefinition processDefinition = ((RepositoryServiceImpl) processEngine.getRepositoryService())
				.getDeployedProcessDefinition(processDefinitionId);
		if (null != processDefinition) {
			return (ProcessDefinitionEntity)processDefinition;
		}
		return null;
	}
}
