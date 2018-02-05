package com.hebabr.base.service.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.stereotype.Service;

import com.hebabr.base.service.TaskFlowService;
import com.hebabr.base.util.ProcessUtil;

@Service
public class TaskFlowServiceImpl implements TaskFlowService {

	private ProcessDefinitionEntity processDefinitionEntity;
	private ProcessEngine processEngine;
	private String processInstanceId;

	public TaskFlowServiceImpl(ProcessEngine processEngine, String processInstanceId) {

		this.processEngine = processEngine;
		this.processInstanceId = processInstanceId;
		String processDefinitionId = processEngine.getRuntimeService().createProcessInstanceQuery().singleResult()
				.getProcessDefinitionId();
		ProcessUtil.getProcessDefinitionEntity(processEngine, processDefinitionId);
	}

	@Override
	public ActivityImpl[] insertTaskAfter(String targetTaskKey, String taskId, String doUserId, String[] names,
			String... assignee) {
		
		return null;
	}

}
