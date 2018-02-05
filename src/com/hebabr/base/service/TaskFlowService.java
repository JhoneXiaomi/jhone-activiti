package com.hebabr.base.service;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

public interface TaskFlowService {

	public ActivityImpl[] insertTaskAfter(String targetTaskKey,String taskId,String doUserId,String[] names,String...assignee);
}
