package com.hebabr.base.service;

import org.activiti.engine.ProcessEngine;

public interface TempActivityService {
	
	/**
	 * @Description 添加流程节点
	 * 
	 * @param processEngine 流程引擎
	 * @param userCode  用户code
	 * @param taskId   任务Id
	 * @param activityName 节点名称
	 * @param doUserId 执行者Id
	 * @return
	 */
	void addActivity(ProcessEngine processEngine,String userCode,String taskId,String activityName,String doUserId);
}
