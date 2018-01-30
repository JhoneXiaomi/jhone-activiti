package com.hebabr.base.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hebabr.base.service.ActivitiAPIUtilService;

/**
 * 
 * @Description activiti 常用的方法接口API实现类方法
 *
 * @author jhone
 * @createTime 2018年1月31日上午1:14:32
 */
@Service
public class ActivitiAPIUtilServiceImpl implements ActivitiAPIUtilService {

	@Autowired 
	private RepositoryService repositoryService;
	@Autowired 
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	
	@Override
	public Deployment deployByBpmnAndImg(String delopmentName, String classForBpmn, String classForImg) {

		// 获取仓库服务，从类路径下完成部署
		Deployment deployment = (Deployment) repositoryService.createDeployment() // 创建部署构造器
				.name(delopmentName) // 添加部署流程的NAME
				.addClasspathResource(classForBpmn) // 定义资源文件BPMN的CLASSPATH
				.addClasspathResource(classForImg) // 定义资源文件IMG的CLASSPATH
				.deploy();// 发布完成
		return deployment;
	}

	@Override
	public Deployment deployByZip(String delopmentName, String zipFile) throws FileNotFoundException {
		File file=new File(zipFile);
		InputStream in=new FileInputStream(file);
		//InputStream in = this.getClass().getClassLoader().getResourceAsStream(zipFile);
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(delopmentName)
				.addZipInputStream(new ZipInputStream(in)).deploy();

		return deployment;
	}

	@Override
	public ProcessInstance startProcessByKey(String processInstanceKey, Map<String, Object> params) {
		ProcessInstance pi = ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceByKey(processInstanceKey, params);// 按照流程定义的key
																		// 启动流程实例，默认是最新版本启动
		return pi;
	}

	public ProcessInstance startProcessById(String processinstanceDefinitionId, Map<String, Object> params) {
		ProcessInstance pi = ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceById(processinstanceDefinitionId, params);// 按照流程定义的Id
																				// 启动流程实例，默认是最新版本启动
		return pi;
	}

	@Override
	public void closeProcess(String processInstanceByKey, Map<String, Object> params) {
		ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceByKey(processInstanceByKey, params);// 按照流程定义的key
																			// 启动流程实例，默认是最新版本启动
	}

	public boolean isCompleted(String processInstanceId) {
		boolean flag = false;
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (null != pi)
			flag = true;
		return flag;
	}

	@Override
	public void completePersonTask(String taskId, Map<String, Object> params) {

		System.out.println(params);
		if(params == null){
			params = new HashMap<String, Object>();
		}
		processEngine.getTaskService().complete(taskId, params);
	}

	public List<ProcessDefinition> findProcessDefinition() {

		List<ProcessDefinition> pList = processEngine.getRepositoryService().createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		return pList;
	}

	
	@Override
	public Task findTaskByTaskId(String taskid) {

		Task task = (Task) processEngine.getTaskService().createTaskQuery().taskId(taskid).singleResult();
		return task;
	}

	public Task findTaskByProcessInstanceId(String processInstanceId) {
		Task task = (Task) processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.singleResult();
		
		return task;
	}

	@Override
	public List<Task> findAllTask() {
		List<Task> lTask = processEngine.getTaskService().createTaskQuery()//
				.orderByTaskCreateTime().desc() // 按照任务的创建时间升序排列
				.list(); // 查询任务的所有记录
		return lTask;
	}

	@Override
	public boolean findProcessState(String processInatanceId) {
		boolean flag = runtimeService.createProcessInstanceQuery().processInstanceId(processInatanceId).singleResult()
				.isEnded();
		return flag;
	}

	@Override
	public List<Task> findHistoryTask() {
		// TODO
		return null;
	}

	@Override
	public void endProcess(String taskId) {
		ActivityImpl endActivity = findActivitiImpl(taskId, "end");
		commitProcess(taskId, null, endActivity.getId());
	}

	@Override
	public void callBackProcess(String taskId, String activityId, Map<String, Object> params) {
		if (StringUtils.isEmpty(activityId))
			try {
				throw new Exception("驳回目标节点ID为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}

		// 查找所有并行任务节点，同时驳回
		List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(),
				findTaskByTaskId(taskId).getTaskDefinitionKey());
		for (Task task : taskList) {
			commitProcess(task.getId(), params, activityId);
		}

	}

	@Override
	public void callBackProcess(String taskId, String activityId) {
		if (StringUtils.isEmpty(activityId)) {
			try {
				throw new Exception("目标节点ID为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 查找所有并行任务节点，同时取回
		List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(),
				findTaskById(taskId).getTaskDefinitionKey());
		for (Task task : taskList) {
			commitProcess(task.getId(), null, activityId);
		}
	}

	@Override
	public void turnTransition(String taskId, String activityId, Map<String, Object> params) {

		// 当前节点
		ActivityImpl currActivity = findActivitiImpl(taskId, null);
		// 清空当前流向
		List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

		// 创建新流向
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		// 目标节点
		ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
		// 设置新流向的目标节点
		newTransition.setDestination(pointActivity);

		// 执行转向任务
		taskService.complete(taskId, params);
		// 删除目标节点新流入
		pointActivity.getIncomingTransitions().remove(newTransition);

		// 还原以前流向
		restoreTransition(currActivity, oriPvmTransitionList);
	}

	@Override
	public List<ActivityImpl> findBackAvtivity(String taskId) {
		// TODO
		return null;
	}

	@Override
	public void commitProcess(String taskId, Map<String, Object> params, String activityId) {
		if (null == params)
			params = new HashMap<String, Object>();
		// 跳转节点为空，默认提交操作
		if (StringUtils.isEmpty(activityId))
			taskService.complete(taskId, params);
		else
			turnTransition(taskId, activityId, params); // 流程转向操作

	}

	@Override
	public ProcessInstance findProcessInstanceByTaskId(String taskId) {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(findTaskByTaskId(taskId).getProcessInstanceId()).singleResult();
		if (processInstance == null) {
			try {
				throw new Exception("流程实例未找到!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return processInstance;
	}

	@Override
	public List<Task> findTaskListByKey(String processInstanceId, String key) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();

	}

	@Override
	public void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {

		// 清空现有流向
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		pvmTransitionList.clear();
		// 还原以前流向
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			pvmTransitionList.add(pvmTransition);
		}
	}

	@Override
	public ActivityImpl findActivitiImpl(String taskId, String activityId) {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

		// 获取当前活动节点ID
		if (StringUtils.isEmpty(activityId)) {
			activityId = findTaskById(taskId).getTaskDefinitionKey();
		}

		// 根据流程定义，获取该流程实例的结束节点
		if (activityId.toUpperCase().equals("END")) {
			for (ActivityImpl activityImpl : processDefinition.getActivities()) {
				List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
				if (pvmTransitionList.isEmpty()) {
					return activityImpl;
				}
			}
		}

		// 根据节点ID，获取对应的活动节点
		ActivityImpl activityImpl = processDefinition.findActivity(activityId);

		return activityImpl;
	}

	@Override
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

		if (processDefinition == null) {
			try {
				throw new Exception("流程定义未找到!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return processDefinition;
	}

	@Override
	public List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
		// 存储当前节点所有流向临时变量
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		// 获取当前节点所有流向，存储到临时变量，然后清空
		List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		return oriPvmTransitionList;
	}

	@Override
	public TaskEntity findTaskById(String taskId) {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			try {
				throw new Exception("任务实例未找到!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return task;
	}

	public void deleteDeloyment(String deploymentId, boolean flag) {
		repositoryService.deleteDeployment(deploymentId, flag);
	}

	public void deleteDeloyment(String delopmentId) {
		repositoryService.deleteDeployment(delopmentId);
	}

	public void deleteAllDeloyment(String delopmentId) {
		List<ProcessDefinition> pList = this.findProcessDefinition();
		for (ProcessDefinition p : pList) {
			repositoryService.deleteDeployment(p.getDeploymentId());
		}
	}

	public void deleteAllDeloyment(String delopmentId, boolean flag) {
		List<ProcessDefinition> pList = this.findProcessDefinition();
		for (ProcessDefinition p : pList) {
			repositoryService.deleteDeployment(p.getDeploymentId(), flag);
		}
	}

	public void setVariables(String taskId, String paramName, String paramValue) {
		TaskService taskService = processEngine.getTaskService();
		taskService.setVariable(taskId, paramName, paramValue);
	}

	public Object findVariables(String executionId , String variableName ) {
		Object paramValue = runtimeService.getVariable(executionId , variableName);
		return paramValue;
	}

	public Map<String, String> getCurrentActivityCoordinates(String taskId) {
		Map<String, String> coordinates = new HashMap<String, String>();
		// 1. 获取到当前活动的ID
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String currentActivitiId = pi.getActivityId();
		// 2. 获取到流程定义
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) processEngine.getRepositoryService()
				.getProcessDefinition(task.getProcessDefinitionId());
		// 3. 使用流程定义通过currentActivitiId得到活动对象
		ActivityImpl activity = pd.findActivity(currentActivitiId);
		// 4. 获取活动的坐标
		coordinates.put("x", activity.getX() + "");
		coordinates.put("y", activity.getY() + "");
		coordinates.put("width", activity.getWidth() + "");
		coordinates.put("height", activity.getHeight() + "");
		return coordinates;
	}

	@Override
	public List<HistoricTaskInstance> findPersonNotFinashedHistoryTask(String assignee) {

		List<HistoricTaskInstance> listTasks = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.taskAssignee(assignee) // 指定办理人
				.list();
		return listTasks;
	}

	@Override
	public List<HistoricTaskInstance> findPersonFinashedHistoryTask(String assignee) {
		List<HistoricTaskInstance> listTasks = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.taskAssignee(assignee) // 指定办理人
				.finished()// 查询已经完结的任务
				.list();
		return listTasks;
	}

	@Override
	public Task findTaskByExecutionId(String executionId) {
		Task task = taskService.createTaskQuery().executionId(executionId).singleResult();
		return task;
	}
	
	@Override
	public ProcessDefinition findProcessDefinitionById(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);

		return processDefinition;
	}

	@Override
	public List<Task> findGroupTask(String candidateUser) {
		List<Task> lTask = taskService.createTaskQuery().taskCandidateUser(candidateUser)// 组任务的办理人查询
				.orderByTaskCreateTime().asc()// 使用创建时间的升序排列
				.list();// 返回列表
		return lTask;
	}

	@Override
	public List<Task> findPersonTask(String assignee) {

		List<Task> list = processEngine.getTaskService() //
				.createTaskQuery()//
				.taskAssignee(assignee) // 指定个人任务的办理人查询任务
				.orderByTaskCreateTime().desc() // 按照任务的创建时间升序排列
				.list(); // 查询任务的所有记录
		return list;
	}

	@Override
	public ActivityImpl getActivityImpl(RepositoryService repositoryService, String processDefId, String activityId) {
		ProcessDefinitionEntity pde = getProcessDefinition(repositoryService, processDefId);
		return (ActivityImpl) pde.findActivity(activityId);
	}

	@Override
	public ProcessDefinitionEntity getProcessDefinition(RepositoryService repositoryService, String processDefId) {

		return (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefId);
	}

	@Override
	public ProcessDefinition findProcessDefinitionByProcessDefinitionId(String processDefinitionId) {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

}
