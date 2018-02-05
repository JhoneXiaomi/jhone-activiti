package com.hebabr.base.service.impl;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.hebabr.base.service.TempActivityService;

@Service
public class TempActivituServiceImpl implements TempActivityService{

	@Override
	public void addActivity(ProcessEngine processEngine, String userCode, String taskId, String activityName,
			String doUserId) {
		
		TaskService taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().taskId(taskId).list();
		if (null != taskList) {
			Task task = taskList.get(0);
			taskService.setAssignee(taskId, doUserId);
			String[] assos = userCode.split(",");
			String[] activityNames = activityName.split(",");
			TaskFlowServiceImpl taskFlowServiceImpl = new TaskFlowServiceImpl(processEngine, task.getProcessInstanceId());
			taskFlowServiceImpl.insertTaskAfter(task.getTaskDefinitionKey(), taskId, doUserId, assos, activityNames);
		}
	}

}
