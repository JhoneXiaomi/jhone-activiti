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

import com.hebabr.approval.pojo.ProcessDefinitionTempPojo;
import com.hebabr.approval.service.IFlowNodeInfoService;
import com.hebabr.approval.service.IFlowNodeService;
import com.hebabr.approval.service.IFlowProcessinstanceService;
import com.hebabr.base.dao.ActHiActinstDtoMapper;
import com.hebabr.base.dao.ActHiProcinstDtoMapper;
import com.hebabr.base.dao.ActHiTaskinstDtoMapper;
import com.hebabr.base.dao.ActRuIdEntitylinkDtoMapper;
import com.hebabr.base.dao.FlowNodeDtoMapper;
import com.hebabr.base.dao.FlowNodeInfoDtoMapper;
import com.hebabr.base.dao.FlowProcessdefinitionDtoMapper;
import com.hebabr.base.dao.FlowProcessinstanceDtoMapper;
import com.hebabr.base.pojo.AssigneePojo;
import com.hebabr.base.pojo.UserPojo;
import com.hebabr.base.service.ActivitiAPIUtilService;
import com.hebabr.base.service.IProcessDefintionService;
import com.hebabr.base.util.UuidUtils;
import com.hebabr.model.ActHiActinstDto;
import com.hebabr.model.ActHiActinstDtoExample;
import com.hebabr.model.ActHiActinstDtoExample.Criteria;
import com.hebabr.model.ActHiProcinstDto;
import com.hebabr.model.ActHiProcinstDtoExample;
import com.hebabr.model.ActHiTaskinstDto;
import com.hebabr.model.ActHiTaskinstDtoExample;
import com.hebabr.model.ActRuIdEntitylinkDto;
import com.hebabr.model.ActRuIdEntitylinkDtoExample;
import com.hebabr.model.FlowNodeDto;
import com.hebabr.model.FlowNodeDtoExample;
import com.hebabr.model.FlowNodeInfoDto;
import com.hebabr.model.FlowNodeInfoDtoExample;
import com.hebabr.model.FlowProcessdefinitionDto;
import com.hebabr.model.FlowProcessdefinitionDtoExample;
import com.hebabr.model.FlowProcessinstanceDto;
import com.hebabr.model.FlowProcessinstanceDtoExample;
import com.hebabr.model.TbUserDto;
import com.hebabr.process.pojo.BackLogProcessinstanceDto;

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
	@Autowired
	private ActHiActinstDtoMapper actHiActinstDtoMapper;
	@Autowired
	private ActHiProcinstDtoMapper actHiProcinstDtoMapper;
	@Autowired
	private FlowProcessinstanceDtoMapper flowProcessinstanceDtoMapper;
	@Autowired
	private ActHiTaskinstDtoMapper actHiTaskinstDtoMapper;
	@Autowired
	private FlowNodeDtoMapper flowNodeDtoMapper;
	@Autowired
	private FlowNodeInfoDtoMapper flowNodeInfoDtoMapper;
	@Autowired
	private ActRuIdEntitylinkDtoMapper actRuIdEntitylinkDtoMapper;
	@Autowired
	private FlowProcessdefinitionDtoMapper flowProcessdefinitionDtoMapper;
	@Autowired
	private IFlowProcessinstanceService iFlowProcessinstanceService;
	@Autowired
	private IFlowNodeInfoService iFlowNodeInfoService;
	@Autowired
	private IProcessDefintionService iProcessDefintionService;
	@Autowired
	private FlowProcessdefinitionDtoMapper flowProcessdefintionDtoMapper;
	@Autowired
	private IFlowNodeService iFlowNodeService;

	// private FlowNodeInfoDtoMapper FlowNodeInfoDtoMapper;
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
	 * 查询所有启用的流程定义
	 * 
	 * @return List<ProcessDefinitionEntity> 流程定义的集合
	 */
	public List<FlowProcessdefinitionDto> findAllAbleProcessDefinition() {

		List<FlowProcessdefinitionDto> listProcesses = new ArrayList<FlowProcessdefinitionDto>();
		List<ProcessDefinitionTempPojo> pds = iProcessDefintionService.findAllProcessDefinitions();
		Map<String, Object> pd = new HashMap<String, Object>();
		for (ProcessDefinitionTempPojo p : pds) {
			pd.put(p.getProcessdefinitionKey(), p.getDevelopmentId());
		}
		Collection<Object> pdc = pd.values();
		for (Object p : pdc) {
			FlowProcessdefinitionDto flowProcessdefintionDto = flowProcessdefintionDtoMapper
					.selectByDevelopmentId((String) p);
			if (flowProcessdefintionDto.getState() == 1)
				listProcesses.add(flowProcessdefintionDtoMapper.selectByDevelopmentId((String) p));
		}
		return listProcesses;
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
	 * 查看个人待办流程
	 * 
	 * @return List<FlowProcessinstanceDto> 个人代办的流程信息
	 */
	@Override
	public List<BackLogProcessinstanceDto> findByUserId(String userId) {

		List<BackLogProcessinstanceDto> personWiteProcess = new ArrayList<BackLogProcessinstanceDto>();
		List<Task> listTask = this.findGroupTask(userId);
		List<Task> personTask = this.findPersonTask(userId);
		for (Task pt : personTask) {
			listTask.add(pt);
		}
		for (Task t : listTask) {
			BackLogProcessinstanceDto flowProcessinstanceDto = iFlowProcessinstanceService
					.findByPiIdAndNodeIdAndUserId(t.getProcessInstanceId(),t.getTaskDefinitionKey(),userId);
			// 去除驳回到当前人的流程任务
			if(null !=flowProcessinstanceDto){
				if(flowProcessinstanceDto.getState()!=4){
					flowProcessinstanceDto.setAcceptTime(t.getCreateTime());
					personWiteProcess.add(flowProcessinstanceDto);
				}
					
			}
		}
		return personWiteProcess;
	}
	
	// 代办流程
	@Override
	public List<FlowProcessinstanceDto> findPersonInstance(String assigneeId) {

		List<FlowProcessinstanceDto> personWiteProcess = new ArrayList<FlowProcessinstanceDto>();
		List<Task> listTask = this.findGroupTask(assigneeId);
		List<Task> personTask = this.findPersonTask(assigneeId);
		for (Task pt : personTask) {
			listTask.add(pt);
		}
		for (Task t : listTask) {
			FlowProcessinstanceDto flowProcessinstanceDto = flowProcessinstanceDtoMapper
					.selectByProcessinstanceId(t.getProcessInstanceId());
			// 去除驳回到当前人的流程任务
			if(null !=flowProcessinstanceDto){
				if(flowProcessinstanceDto.getState()!=4){
					flowProcessinstanceDto.setAcceptTime(t.getCreateTime());
					personWiteProcess.add(flowProcessinstanceDto);
				}
					
			}
		}
		return personWiteProcess;
	}
	/**
	 * 查看个人被驳回的流程 包流程未结束 flow_processinstance state="" 流程定义Id ——》节点表——》 审批用户组表
	 */
	@Override
	public List<FlowProcessinstanceDto> findPersonCallbackProcessInstance(HttpSession session, String assigneId) {
		List<ProcessInstance> lp = runtimeService.createProcessInstanceQuery().list();// 当前正在运行的流程
		List<FlowProcessinstanceDto> callbaceProcesses = new ArrayList<FlowProcessinstanceDto>();
		for (ProcessInstance p : lp) {
			ActHiTaskinstDtoExample actHiTaskinstDtoExample = new ActHiTaskinstDtoExample();
			com.hebabr.model.ActHiTaskinstDtoExample.Criteria ahc = actHiTaskinstDtoExample.createCriteria();
			ahc.andAssigneeEqualTo(assigneId);
			ahc.andProcInstIdEqualTo(p.getId());
			List<ActHiTaskinstDto> listTask = actHiTaskinstDtoMapper.selectByExample(actHiTaskinstDtoExample);

			if (listTask.size() >= 2) {
				FlowProcessinstanceDto flowProcessinstanceDto = flowProcessinstanceDtoMapper
						.selectByProcessinstanceId(p.getId());
				if(null!=flowProcessinstanceDto)
						callbaceProcesses.add(flowProcessinstanceDto);
			}
		}
		return callbaceProcesses;
	}

	/**
	 * 
	 * 查看发起的流程
	 * 
	 * 未终止流程 flow_processinstance state="" 发起人 是我
	 * 
	 */
	@Override
	public List<FlowProcessinstanceDto> findPersonInstanceStartWithMe(HttpSession session, String assigneId) {
		UserPojo userPojo = (UserPojo) session.getAttribute("currentUser");
		FlowProcessinstanceDtoExample flowProcessinstanceDtoExample = new FlowProcessinstanceDtoExample();
		com.hebabr.model.FlowProcessinstanceDtoExample.Criteria fic = flowProcessinstanceDtoExample.createCriteria();
		fic.andAssigneeIdEqualTo(userPojo.getUserId());
		fic.andStateNotEqualTo(4);
		List<FlowProcessinstanceDto> listFlowProcesses = flowProcessinstanceDtoMapper
				.selectByExample(flowProcessinstanceDtoExample);
		if (listFlowProcesses.size() > 0)
			listFlowProcesses.get(0);

		return listFlowProcesses;
	}
	
	/**
	 * 
	 * 查看发起的流程APP
	 * 
	 * 未终止流程 flow_processinstance state="" 发起人 是我
	 * 
	 */
	@Override
	public List<FlowProcessinstanceDto> appfindPersonInstanceStartWithMe(TbUserDto user, String assigneId) {

		FlowProcessinstanceDtoExample flowProcessinstanceDtoExample = new FlowProcessinstanceDtoExample();
		com.hebabr.model.FlowProcessinstanceDtoExample.Criteria fic = flowProcessinstanceDtoExample.createCriteria();
		fic.andAssigneeIdEqualTo(user.getId());
		fic.andStateNotEqualTo(4);
		List<FlowProcessinstanceDto> listFlowProcesses = flowProcessinstanceDtoMapper
				.selectByExample(flowProcessinstanceDtoExample);
		if (listFlowProcesses.size() > 0)
			listFlowProcesses.get(0);

		return listFlowProcesses;
	}

	/**
	 * 查看我处理过的流程 正在執行，我不是發起人，我是參與者
	 * 
	 */
	@Override
	public List<FlowProcessinstanceDto> findPersonInstanceDoneWithMe(HttpSession session, String assigneId) {

		String currentId = "";
		UserPojo userPojo = (UserPojo) session.getAttribute("currentUser");
		if (userPojo == null) {
			currentId = assigneId;
		} else {
			currentId = userPojo.getUserId();
		}
		FlowProcessinstanceDtoExample flowProcessinstanceDtoExample = new FlowProcessinstanceDtoExample();
		com.hebabr.model.FlowProcessinstanceDtoExample.Criteria fic = flowProcessinstanceDtoExample.createCriteria();
		fic.andStateNotEqualTo(4); // 當前流程沒有結束
		fic.andAssigneeIdNotEqualTo(currentId); // 不是發起人
		List<FlowProcessinstanceDto> listFlowProcesses = flowProcessinstanceDtoMapper
				.selectByExample(flowProcessinstanceDtoExample);
		List<FlowProcessinstanceDto> endProcesses = new ArrayList<FlowProcessinstanceDto>();
		if (listFlowProcesses.size() > 0)
			for (FlowProcessinstanceDto flowProcess : listFlowProcesses) {
				ActRuIdEntitylinkDtoExample actRuIdEntitylinkDtoE = new ActRuIdEntitylinkDtoExample();
				com.hebabr.model.ActRuIdEntitylinkDtoExample.Criteria acc = actRuIdEntitylinkDtoE.createCriteria();
				String processinstance = flowProcess.getProcessinstanceId();
				if (null != processinstance)
					acc.andProcInstIdEqualTo(flowProcess.getProcessinstanceId());
				acc.andUserIdEqualTo(currentId);
				List<ActRuIdEntitylinkDto> lfw = actRuIdEntitylinkDtoMapper.selectByExample(actRuIdEntitylinkDtoE);
				if (lfw.size() > 0)
					endProcesses.add(flowProcess);
			}
		return endProcesses;
	}

	/**
	 * 
	 * 办结流程
	 * 
	 * 包含我的結束流程 flow_processinstance state="完結" 流程定义Id ——》节点表——》 审批用户组表
	 */
	@Override
	public List<FlowProcessinstanceDto> findPersonInstanceContaintMeEnd(String assigneId) {

		List<FlowProcessinstanceDto> endProcesses = new ArrayList<FlowProcessinstanceDto>();
		ActHiProcinstDtoExample ape = new ActHiProcinstDtoExample();
		com.hebabr.model.ActHiProcinstDtoExample.Criteria apc = ape.createCriteria();
		apc.andEndActIdNotEqualTo("");// 结束的流程
		List<ActHiProcinstDto> lHisProcess = actHiProcinstDtoMapper.selectByExample(ape);

		for (ActHiProcinstDto lp : lHisProcess) {

			ActHiActinstDtoExample ae = new ActHiActinstDtoExample();
			Criteria ace = ae.createCriteria();
			ace.andAssigneeEqualTo(assigneId);
			ace.andProcInstIdEqualTo(lp.getProcInstId());
			List<ActHiActinstDto> actHiActinstDtoList = actHiActinstDtoMapper.selectByExample(ae);
			if (null != actHiActinstDtoList && actHiActinstDtoList.size() > 0) {
				FlowProcessinstanceDto flowProcessinstanceDto = flowProcessinstanceDtoMapper
						.selectByProcessinstanceId(actHiActinstDtoList.get(0).getProcInstId());
				endProcesses.add(flowProcessinstanceDto);
			}
		}

		return endProcesses;
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

	/**
	 * 本地静态资源读取bpmn文件以获取对应的节点信息
	 * 
	 * create by zhangzhiwei 2017/02/24 PM9:17:23
	 * 
	 * @param session
	 *            会话作用域
	 * @param processDefinitionId
	 *            流程定义Id
	 */
	@Override
	public void readProcessDefindNodeFromLocahost(HttpSession session, String filePath) {
		UserPojo userPojo = (UserPojo) session.getAttribute("currentUser");
		InputStream resouceStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in;
		XMLStreamReader xtr;
		try {
			in = new InputStreamReader(resouceStream, "UTF-8");
			xtr = xif.createXMLStreamReader(in);
			BpmnModel model = new BpmnXMLConverter().convertToBpmnModel(xtr);
			Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
			for (FlowElement e : flowElements) {
				// 获取流程定义中的开始、结束、用户节点
				if (e.getClass().toString().contains("UserTask")) {
					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					// flowNodeDto.setProcessdefineId(processDefinitionId);
					// flowNodeDto.setProcessdefineName(pd.getName());
					// flowNodeDto.setNodeName(e.getName());
					// flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userPojo.getUserId());
					flowNodeDto.setNodeType("UserTask");
					// TODO group_id
					flowNodeDtoMapper.insert(flowNodeDto);
				} else if (e.getClass().toString().contains("StartEvent")) {
					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					// flowNodeDto.setProcessdefineId(processDefinitionId);
					// flowNodeDto.setProcessdefineName(pd.getName());
					// flowNodeDto.setNodeName(e.getName());
					// flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userPojo.getUserId());
					flowNodeDto.setNodeType("StartEvent");
					// TODO group_id
					flowNodeDtoMapper.insert(flowNodeDto);
				} else if (e.getClass().toString().contains("EndEvent")) {

					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					// flowNodeDto.setProcessdefineId(processDefinitionId);
					// flowNodeDto.setProcessdefineName(pd.getName());
					// flowNodeDto.setNodeName(e.getName());
					// flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userPojo.getUserId());
					flowNodeDto.setNodeType("EndEvent");
					// TODO group_id
					flowNodeDtoMapper.insert(flowNodeDto);
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从数据库中部署的流程定义读取对应的节点信息
	 * 
	 * create by zhangzhiwei 2017/02/24 PM4:54:23
	 * 
	 * @param session
	 *            会话作用域
	 * @param processDefinitionId
	 *            流程定义Id
	 */
	@Override
	public void readProcessDefindNodeFromNetwork(String userId, String processDefinitionId) {

		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
				.singleResult();
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		System.out.println(BpmnModel.class.toString());
		if (model != null) {
			Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
			int i = 1;
			for (FlowElement e : flowElements) {
				// 获取流程定义中的开始、结束、用户节点
				if (e.getClass().toString().contains("UserTask") && i==2) {
					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					flowNodeDto.setProcessdefineId(processDefinitionId);
					flowNodeDto.setProcessdefineName(pd.getName());
					flowNodeDto.setNodeName(e.getName());
					flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userId);
					flowNodeDto.setNodeType("UserTask");
					flowNodeDto.setIsSelect(1); // 默认为可选项
					flowNodeDto.setNodeAssigneeType(1);
					flowNodeDto.setNodeOrder(i);
					flowNodeDto.setIsInitiator(true);
					flowNodeDto.setIsAllPerson(true);
					ActivityImpl activityImpl = getActivityImpl(repositoryService, processDefinitionId, e.getId());
					TaskDefinition t = (TaskDefinition) activityImpl.getProperty("taskDefinition");

					if (null == t.getAssigneeExpression())
						flowNodeDto.setNodeParam(
								t.getCandidateUserIdExpressions().toString().replaceAll("[^a-z^A-Z]", ""));
					else
						flowNodeDto.setNodeParam(t.getAssigneeExpression().toString().replaceAll("[^a-z^A-Z]", ""));
					flowNodeDtoMapper.insert(flowNodeDto);

					// 保存信息到节点點信息表
				}else if(e.getClass().toString().contains("UserTask")){
					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					flowNodeDto.setProcessdefineId(processDefinitionId);
					flowNodeDto.setProcessdefineName(pd.getName());
					flowNodeDto.setNodeName(e.getName());
					flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userId);
					flowNodeDto.setNodeType("UserTask");
					flowNodeDto.setIsSelect(1); // 默认为可选项
					flowNodeDto.setNodeAssigneeType(1);
					flowNodeDto.setNodeOrder(i);
					flowNodeDto.setIsInitiator(false);
					flowNodeDto.setIsAllPerson(true);
					ActivityImpl activityImpl = getActivityImpl(repositoryService, processDefinitionId, e.getId());
					TaskDefinition t = (TaskDefinition) activityImpl.getProperty("taskDefinition");

					if (null == t.getAssigneeExpression())
						flowNodeDto.setNodeParam(
								t.getCandidateUserIdExpressions().toString().replaceAll("[^a-z^A-Z]", ""));
					else
						flowNodeDto.setNodeParam(t.getAssigneeExpression().toString().replaceAll("[^a-z^A-Z]", ""));
					flowNodeDtoMapper.insert(flowNodeDto);
				}else if (e.getClass().toString().contains("StartEvent")) {
					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					flowNodeDto.setProcessdefineId(processDefinitionId);
					flowNodeDto.setProcessdefineName(pd.getName());
					flowNodeDto.setNodeName(e.getName());
					flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userId);
					flowNodeDto.setNodeType("StartEvent");
					flowNodeDto.setIsSelect(1);  // 默认为可选项
					flowNodeDto.setNodeOrder(i);
					flowNodeDto.setNodeAssigneeType(1);
					flowNodeDto.setIsInitiator(false);
					flowNodeDto.setIsAllPerson(true);
					flowNodeDtoMapper.insert(flowNodeDto);
				} else if (e.getClass().toString().contains("EndEvent")) {

					FlowNodeDto flowNodeDto = new FlowNodeDto();
					flowNodeDto.setNodeId(e.getId());
					flowNodeDto.setProcessdefineId(processDefinitionId);
					flowNodeDto.setProcessdefineName(pd.getName());
					flowNodeDto.setNodeName(e.getName());
					flowNodeDto.setVersion(pd.getVersion());
					flowNodeDto.setCreateTime(new Date());
					flowNodeDto.setCreateId(userId);
					flowNodeDto.setNodeType("EndEvent");
					flowNodeDto.setIsSelect(1); // 默认为可选项
					flowNodeDto.setNodeOrder(i);
					flowNodeDto.setNodeAssigneeType(1);
					flowNodeDto.setIsInitiator(false);
					flowNodeDto.setIsAllPerson(true);
					flowNodeDtoMapper.insert(flowNodeDto);
				}
				i++;
			}
		}
	}

	/**
	 * 
	 * 获取当前节点的信息
	 */
	@Override
	public FlowNodeInfoDto findCurrentNodeInfo(String processdefinitionId, String assigneeId, int state) {

		List<HistoricActivityInstance> currentNode = historyService.createHistoricActivityInstanceQuery()
				.processDefinitionId(processdefinitionId) // 流程Id
				.taskAssignee(assigneeId)// 当前用户
				.list();
		FlowNodeInfoDto nodeInfo = new FlowNodeInfoDto();
		String currentNodeId = "";
		List<FlowNodeDto> listNode = new ArrayList<FlowNodeDto>();
		FlowNodeDtoExample flowNodeDtoE = new FlowNodeDtoExample();
		com.hebabr.model.FlowNodeDtoExample.Criteria fnc = flowNodeDtoE.createCriteria();
		if (currentNode.size() > 0) {
			currentNodeId = currentNode.get(0).getActivityId();
			fnc.andProcessdefineIdEqualTo(processdefinitionId);
			fnc.andNodeIdEqualTo(currentNodeId);
			listNode = flowNodeDtoMapper.selectByExample(flowNodeDtoE);
		}
		if (listNode.size() > 0) {
			FlowNodeInfoDtoExample flowNodeInfoDtoE = new FlowNodeInfoDtoExample();
			com.hebabr.model.FlowNodeInfoDtoExample.Criteria fni = flowNodeInfoDtoE.createCriteria();
			fni.andFlowNodeIdEqualTo(listNode.get(0).getId());
			fni.andProcessdefineIdEqualTo(processdefinitionId);
			List<FlowNodeInfoDto> listnodeInfo = flowNodeInfoDtoMapper.selectByExample(flowNodeInfoDtoE);
			if (listnodeInfo.size() > 0) {
				nodeInfo.setState(state);
				flowNodeInfoDtoMapper.updateByPrimaryKey(nodeInfo);
			}
		}

		// ActivityImpl activityImpl =
		// processDefinitionEntity.findActivity(activityId);//当前节点
		return nodeInfo;
	}

	/**
	 * 
	 * 根据state提供用户使用的模板
	 */
	@Override
	public List<FlowProcessdefinitionDto> findProcessdefinitionByState(int state) {
		FlowProcessdefinitionDtoExample flowProcessdefinitionDtoExample = new FlowProcessdefinitionDtoExample();
		com.hebabr.model.FlowProcessdefinitionDtoExample.Criteria fpc = flowProcessdefinitionDtoExample
				.createCriteria();
		fpc.andStateEqualTo(state); // 表示启用
		List<FlowProcessdefinitionDto> listFlowProcessinstance = flowProcessdefinitionDtoMapper
				.selectByExample(flowProcessdefinitionDtoExample);
		return listFlowProcessinstance;
	}

	/**
	 * 
	 * 启动流程定义
	 */
	@Override
	public boolean startProcessInstance(HttpSession session,String flowProcessinstanceKeyId,String processinstanceDefinitionId,
			Map<String, Object> params, String title) {

		boolean flag = true;
		UserPojo userPojo = (UserPojo) session.getAttribute("currentUser");
		ProcessInstance processInstance = this.startProcessById(processinstanceDefinitionId, params);// 启动流程
		String processInstanceId = processInstance.getProcessInstanceId();
		if (null != processInstance)
			flag = false;
		// 获取所有的节点定义信息根据流程定义id
		List<FlowNodeDto> listNode = iFlowNodeService.findNodesByProcessdefinitionId(processinstanceDefinitionId);
		// 初始化节点信息表
		for (FlowNodeDto node : listNode) {
			FlowNodeInfoDto flowNodeInfoDto = new FlowNodeInfoDto();
			flowNodeInfoDto.setState(2);
			flowNodeInfoDto.setFlowNodeId(node.getNodeId());
			flowNodeInfoDto.setCreateTime(new Date());
			flowNodeInfoDto.setProcessdefineId(processinstanceDefinitionId);
			flowNodeInfoDto.setProcessinstanceId(processInstanceId);
			flowNodeInfoDto.setCreateId(userPojo.getUserId());
			flowNodeInfoDto.setAssigneeId(UuidUtils.generateLongUuid());
			flowNodeInfoDto.setProcessinstanceId(processInstanceId);
			flowNodeInfoDto.setFlowNodeId(node.getId());
			flowNodeInfoDtoMapper.insert(flowNodeInfoDto);
		}
		session.setAttribute("processInstanceId", processInstance.getProcessInstanceId());

		return flag;
	}
	
	/**
	 * 
	 * 启动流程定义
	 */
	@Override
	public boolean startProcessInstanceApp(TbUserDto user,HttpSession session,String flowProcessinstanceKeyId,String processinstanceDefinitionId,
			Map<String, Object> params, String title) {

		boolean flag = true;
		//UserPojo userPojo = (UserPojo) session.getAttribute("currentUser");
		ProcessInstance processInstance = this.startProcessById(processinstanceDefinitionId, params);// 启动流程
		String processInstanceId = processInstance.getProcessInstanceId();
		if (null != processInstance)
			flag = false;
		// 获取所有的节点定义信息根据流程定义id
		List<FlowNodeDto> listNode = iFlowNodeService.findNodesByProcessdefinitionId(processinstanceDefinitionId);
		// 初始化节点信息表
		for (FlowNodeDto node : listNode) {
			FlowNodeInfoDto flowNodeInfoDto = new FlowNodeInfoDto();
			flowNodeInfoDto.setState(2);
			flowNodeInfoDto.setFlowNodeId(node.getNodeId());
			flowNodeInfoDto.setCreateTime(new Date());
			flowNodeInfoDto.setProcessdefineId(processinstanceDefinitionId);
			flowNodeInfoDto.setProcessinstanceId(processInstanceId);
			flowNodeInfoDto.setCreateId(user.getId());
			flowNodeInfoDto.setAssigneeId(UuidUtils.generateLongUuid());
			flowNodeInfoDto.setProcessinstanceId(processInstanceId);
			flowNodeInfoDto.setFlowNodeId(node.getId());
			flowNodeInfoDtoMapper.insert(flowNodeInfoDto);
		}
		session.setAttribute("processInstanceId", processInstance.getProcessInstanceId());

		return flag;
	}

	/**
	 * 初始化节点的方法
	 * 
	 * 1. 获取所有的流程定义的节点信息，判断是否为可选操作节点 1. 如果isSelect=1 不做处理 2. 如果isSelect = 2
	 * 则需要将给当前节点设定审批人，（其实为空则不做处理,表示当前为申请人）
	 */
	@Override
	public Map<String, Object> startNodeParamInit(String processDefinitionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<FlowNodeDto> nodes = iFlowNodeService.findNodesByProcessdefinitionIdAndIsNotSelect(processDefinitionId);

		for (FlowNodeDto node : nodes) {
			List<AssigneePojo> assigneeIds = new ArrayList<AssigneePojo>();
			assigneeIds = flowNodeDtoMapper.selectUserByProcessDefinitionId(node.getProcessdefineId());
			String str = null;
			for (int i = 1; i < assigneeIds.size(); i++) {
				if (i == 1)
					str = assigneeIds.get(i).getUserId();
				else
					str = str + assigneeIds.get(i).getUserId();
			}
			if (null != assigneeIds && assigneeIds.size() > 0)
				params.put(assigneeIds.get(0).getNodeParam(), str);
		}
		return params;
	}

	/**
	 * 
	 * 查询流程申请人的信息
	 */
	@Override
	public TbUserDto findAssigneeUser(String processInstance) {

		return null;
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

}
