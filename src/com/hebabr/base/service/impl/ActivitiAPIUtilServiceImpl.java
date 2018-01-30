package com.hebabr.base.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hebabr.base.service.ActivitiAPIUtilService;
import com.hebabr.base.util.UuidUtils;

/**
 * 
 * activiti 常用的方法接口API实现类方法
 * 
 * 
 * @author zhangzhiwei
 * 
 * @version 1.0.0
 */
@Service
public class ActivitiAPIUtilServiceImpl implements ActivitiAPIUtilService {

	@Autowired // 注入
	private RepositoryService repositoryService;
	@Autowired // 注入
	private TaskService taskService;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	/*
	 * * 部署流程
	 * 
	 * @param classForBpmn 加载的bpmn 文件路径
	 * 
	 * @param classForImg 加载的Img 文件路径
	 * 
	 * @return Deployment 已发布流程
	 */
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

	/**
	 * 
	 * 部署流程
	 * 
	 * @param classForBpmn
	 *            加载的bpmn 文件路径
	 * @throws FileNotFoundException 
	 */
	@Override
	public Deployment deployByZip(String delopmentName, String zipFile) throws FileNotFoundException {
		File file=new File(zipFile);
		InputStream in=new FileInputStream(file);
		//InputStream in = this.getClass().getClassLoader().getResourceAsStream(zipFile);
		Deployment deployment = processEngine.getRepositoryService().createDeployment().name(delopmentName)
				.addZipInputStream(new ZipInputStream(in)).deploy();

		return deployment;
	}

	/**
	 * 
	 * 根据流程定义的Key启动流程
	 * 
	 * @param processInstancekey
	 *            流程KEY
	 * @param params
	 *            map 类型传入的参数
	 * @return processInstance 返回流程
	 * 
	 */
	@Override
	public ProcessInstance startProcessByKey(String processInstanceKey, Map<String, Object> params) {
		ProcessInstance pi = ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceByKey(processInstanceKey, params);// 按照流程定义的key
																		// 启动流程实例，默认是最新版本启动
		return pi;
	}

	/**
	 * 
	 * 通过流程Id启动流程
	 * 
	 * @param zipFile
	 *            加载的zip压缩文件路径，文件包含bpmn文件和img文件
	 * 
	 */
	public ProcessInstance startProcessById(String processinstanceDefinitionId, Map<String, Object> params) {
		ProcessInstance pi = ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceById(processinstanceDefinitionId, params);// 按照流程定义的Id
																				// 启动流程实例，默认是最新版本启动
		return pi;
	}

	/**
	 * 
	 * 关闭流程
	 * 
	 * @param zipFile
	 *            加载的zip压缩文件路径，文件包含bpmn文件和img文件
	 * 
	 */
	@Override
	public void closeProcess(String processInstanceByKey, Map<String, Object> params) {
		ProcessEngines.getDefaultProcessEngine().getRuntimeService()// 获取正在执行的Service
				.startProcessInstanceByKey(processInstanceByKey, params);// 按照流程定义的key
																			// 启动流程实例，默认是最新版本启动
	}

	/**
	 * 
	 * 查看流程状态
	 * 
	 * @param processInstanceId
	 * 
	 */
	public boolean isCompleted(String processInstanceId) {
		boolean flag = false;
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (null != pi)
			flag = true;
		return flag;
	}

	/**
	 * 
	 * 完成任务
	 * 
	 * @param taskId
	 *            任务ID
	 * @param params
	 *            map类型传入的参数
	 */
	@Override
	public void completePersonTask(String taskId, Map<String, Object> params) {

		System.out.println(params);
		if(params == null){
			params = new HashMap<String, Object>();
		}
		processEngine.getTaskService().complete(taskId, params);
	}

	/**
	 * 
	 * 查询流程定义
	 * 
	 * @return List<ProcessDefinitionEntity> 流程定义的集合
	 */
	public List<ProcessDefinition> findProcessDefinition() {

		List<ProcessDefinition> pList = processEngine.getRepositoryService().createProcessDefinitionQuery()
				.orderByProcessDefinitionVersion().asc().list();
		return pList;
	}

	/**
	 * 
	 * 通过taskId 获取任务
	 * 
	 * @param assignee
	 *            执行者名称
	 * 
	 * @return Task 任务
	 */
	@Override
	public Task findTaskByTaskId(String taskid) {

		Task task = (Task) processEngine.getTaskService().createTaskQuery().taskId(taskid).singleResult();
		return task;
	}

	/**
	 * 
	 * 查询任务通过流程Id
	 * 
	 * @param processInstanceId
	 *            流程Id
	 * @return Task 执行任务
	 */
	public Task findTaskByProcessInstanceId(String processInstanceId) {
		Task task = (Task) processEngine.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.singleResult();
		
		return task;
	}

	/**
	 * 
	 * 获取所有任务
	 * 
	 * @return List<Task> 执行者任务集合
	 */
	@Override
	public List<Task> findAllTask() {
		List<Task> lTask = processEngine.getTaskService().createTaskQuery()//
				.orderByTaskCreateTime().desc() // 按照任务的创建时间升序排列
				.list(); // 查询任务的所有记录
		return lTask;
	}

	/**
	 * 
	 * 查询流程状态
	 * 
	 * @param processInatanceId
	 *            流程Id值
	 * @return boolean 返回结果
	 */
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

	/**
	 * 流程转向操作
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param params
	 *            流程变量
	 * @throws Exception
	 */
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

	/**
	 * 取回流程
	 * 
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            取回节点ID
	 * @throws Exception
	 */
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

	/**
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws Exception
	 */
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

	/**
	 * 根据任务ID获取对应的流程实例
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 根据流程实例ID和任务key值查询所有同级任务集合
	 * 
	 * @param processInstanceId
	 * @param key
	 * @return
	 */
	@Override
	public List<Task> findTaskListByKey(String processInstanceId, String key) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();

	}

	/**
	 * 还原指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @param oriPvmTransitionList
	 *            原有节点流向集合
	 */
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

	/**
	 * 清空指定活动节点流向
	 * 
	 * @param activityImpl
	 *            活动节点
	 * @return 节点流向集合
	 */
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

	// 获取当前节点ID
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

	/**
	 * 
	 * 删除流程定义,包含关联表的全部删除
	 * 
	 * @param delopmentId
	 *            流程的Id
	 * 
	 */
	public void deleteDeloyment(String delopmentId, boolean flag) {
		repositoryService.deleteDeployment(delopmentId, flag);
	}

	/**
	 * 
	 * 删除流程定义,不包含关联表的全部删除
	 * 
	 * @param delopmentId
	 *            流程的Id
	 * 
	 */
	public void deleteDeloyment(String delopmentId) {
		repositoryService.deleteDeployment(delopmentId);
	}

	/**
	 * 
	 * 删除所有的流程,不包含关联的删除
	 * 
	 * @param delopmentId
	 *            流程的Id
	 * 
	 */
	public void deleteAllDeloyment(String delopmentId) {
		List<ProcessDefinition> pList = this.findProcessDefinition();
		for (ProcessDefinition p : pList) {
			repositoryService.deleteDeployment(p.getDeploymentId());
		}
	}

	/**
	 * 
	 * 删除所有的流程,包含关联的删除
	 * 
	 * @param delopmentId
	 *            流程的Id
	 * 
	 */
	public void deleteAllDeloyment(String delopmentId, boolean flag) {
		List<ProcessDefinition> pList = this.findProcessDefinition();
		for (ProcessDefinition p : pList) {
			repositoryService.deleteDeployment(p.getDeploymentId(), flag);
		}
	}


	/**
	 * 
	 * 设置流程变量
	 * 
	 * @param taskId
	 *            任务Id
	 * @param paramName
	 *            变量名称
	 * @param paramValue
	 *            变量值
	 */
	public void setVariables(String taskId, String paramName, String paramValue) {
		TaskService taskService = processEngine.getTaskService();
		taskService.setVariable(taskId, paramName, paramValue);
	}

	/**
	 * 
	 * 获取流程变量
	 * 
	 * @param taskId
	 *            任务Id
	 * @param paramName
	 *            变量名称
	 * @return Object 返回变量值
	 */
	public Object findVariables(String processInstanceId, String paramName) {
		Object paramValue = runtimeService.getVariable(processInstanceId, paramName);
		return paramValue;
	}

	/**
	 * 获取到当前任务的图中位置信息
	 * 
	 * @param taskId
	 *            任务的Id值
	 * @return 当前任务节点的位置信息
	 */
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

	/**
	 * 
	 * 查询已经未结束流程的个人任务
	 * 
	 * @param String
	 *            assignee 任务办理人
	 * @return List<Task> 任务集合
	 */
	@Override
	public List<HistoricTaskInstance> findPersonNotFinashedHistoryTask(String assignee) {

		List<HistoricTaskInstance> listTasks = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.taskAssignee(assignee) // 指定办理人
				.list();
		return listTasks;
	}

	/**
	 * 
	 * 查询已经结束流程的个人任务
	 * 
	 * @param String
	 *            assignee 任务办理人
	 * @return List<Task> 任务集合
	 */
	@Override
	public List<HistoricTaskInstance> findPersonFinashedHistoryTask(String assignee) {
		List<HistoricTaskInstance> listTasks = historyService.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.taskAssignee(assignee) // 指定办理人
				.finished()// 查询已经完结的任务
				.list();
		return listTasks;
	}

	/**
	 * 
	 * 查询任务
	 * 
	 * @param processDefinedId
	 *            流程定义的Id
	 * @return Task 执行任务
	 */
	@Override
	public Task findTaskByExecutionId(String executionId) {
		Task task = taskService.createTaskQuery().executionId(executionId).singleResult();
		return task;
	}
	

	/**
	 * 
	 * 根据流程定义id获取流程定义
	 * 
	 * @param processDefinitionId
	 *            流程定义Id
	 * @return
	 */
	@Override
	public ProcessDefinition findProcessDefinitionById(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);

		return processDefinition;
	}

	/**
	 * 查看用户组任务
	 * 
	 * @return List<Task> 当前用户的组任务集合
	 * 
	 */
	@Override
	public List<Task> findGroupTask(String assigneeId) {
		List<Task> lTask = taskService.createTaskQuery().taskCandidateUser(assigneeId)// 组任务的办理人查询
				.orderByTaskCreateTime().asc()// 使用创建时间的升序排列
				.list();// 返回列表
		return lTask;
	}

	/**
	 * 
	 * 获取个人任务
	 * 
	 * @param assignee
	 *            执行者名称
	 * @return List<Task> 执行者任务集合
	 */
	@Override
	public List<Task> findPersonTask(String assignee) {

		List<Task> list = processEngine.getTaskService() //
				.createTaskQuery()//
				.taskAssignee(assignee) // 指定个人任务的办理人查询任务
				.orderByTaskCreateTime().desc() // 按照任务的创建时间升序排列
				.list(); // 查询任务的所有记录
		return list;
	}

	// 获取流程
	@Override
	public ActivityImpl getActivityImpl(RepositoryService repositoryService, String processDefId, String activityId) {
		ProcessDefinitionEntity pde = getProcessDefinition(repositoryService, processDefId);
		return (ActivityImpl) pde.findActivity(activityId);
	}

	// 获取流程定义实例
	@Override
	public ProcessDefinitionEntity getProcessDefinition(RepositoryService repositoryService, String processDefId) {

		return (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefId);
	}

	// 根据流程定义id 获取对应的流程定义
	@Override
	public ProcessDefinition findProcessDefinitionByProcessDefinitionId(String processDefinitionId) {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	@Override
	public void readProcessDefindNodeFromLocahost(HttpSession session, String filePath) {
		// FIXME Auto-generated method stub
		
	}

	@Override
	public void readProcessDefindNodeFromNetwork(String userId, String processDefinitionId) {
		// FIXME Auto-generated method stub
		
	}

	@Override
	public boolean startProcessInstance(HttpSession session, String flowProcessinstanceKeyId,
			String processinstanceDefinitionId, Map<String, Object> params, String title) {
		// FIXME Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> startNodeParamInit(String processDefinitionId) {
		// FIXME Auto-generated method stub
		return null;
	}

}
