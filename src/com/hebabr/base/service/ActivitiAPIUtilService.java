package com.hebabr.base.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


/**
 * 
 * @Description 封装了activiti 常用的方法接口API
 *
 * @author jhone
 * @createTime 2018年1月31日上午12:41:09
 */
public interface ActivitiAPIUtilService {

	/**
	 * @Description 部署流程
	 * 
	 * @param delopmentName 加载的bpmn 文件路径
	 * @param classForBpmn  加载的Img 文件路径
	 * @param classForImg   已发布流程
	 * @return
	 */
	public Deployment deployByBpmnAndImg(String delopmentName, String classForBpmn,String classForImg);
	
	/**
	 * 
	 * @Description 部署流程
	 * 
	 * @param classForBpmn 加载的bpmn 文件路径
	 * @throws FileNotFoundException 
	 */
	public Deployment deployByZip(String delopmentName,String zipFile) throws FileNotFoundException;
	
	/**
	 * 
	 * @Description 根据流程定义的Key启动流程
	 * 
	 * @param processInstancekey   流程KEY
	 * @param params 类型传入的参数
	 * @return processInstance 返回流程
	 */
	public ProcessInstance startProcessByKey(String processInstanceKey,Map<String,Object> params);
	

	/**
	 * @Description 通过流程Id启动流程
	 * 
	 * @param zipFile 加载的zip压缩文件路径，文件包含bpmn文件和img文件
	 */
	public ProcessInstance startProcessById(String processinstanceDefinitionId,Map<String,Object> params);
	
	/**
	 * 
	 * @Description  查看流程状态
	 * 
	 * @param processInstanceId 流程实例ID
	 * @return 
	 */
	public boolean isCompleted(String processInstanceId);
	
	/**
	 * 
	 * @Description 完成任务
	 *
	 * @param taskId 当前的任务ID
	 * @param params map类型传入的参数
	 */
	public void completePersonTask(String taskId, Map<String, Object> params);
	/**
	 * 
	 * @Description 查看流程定义
	 *
	 * @return List<ProcessDefinition> 流程定义的集合
	 */
    public List<ProcessDefinition> findProcessDefinition();
    
    /**
     * 
     * @Description 获取个人任务
     *
     * @param assignee 执行者名称
     * @return List<Task> 执行者任务集合
     */
    public List<Task> findPersonTask(String assigneeName);
    
    /**
     * 
     * @Description 通过taskId 获取任务
     *
     * @param taskId 当前任务ID
     * @return Task 任务
     */
    public Task findTaskByTaskId(String taskId);
    
    /**
     * 
     * @Description 查询任务
     *
     * @param executionId 流程定义的Id
     * @return  执行任务
     */
    public Task findTaskByExecutionId(String executionId);
    
    /**
     * 
     * @Description 获取所有任务
     *
     * @return List<Task> 执行者任务集合
     */
    public List<Task> findAllTask();
    
    /**
     * 
     * @Description  查询流程状态
     *
     * @param processInatanceId 流程Id值
     * @return boolean 返回流程状态
     */
    public boolean findProcessState(String processInatanceId);
    
    /**
     * 
     * @Description 流程转向操作
     *
     * @param taskId  当前任务ID
     * @param activityId 目标节点任务ID
     * @param params 流程变量
     */
    public void callBackProcess(String taskId, String activityId,Map<String, Object> params);
    
    /**
     * 
     * @Description 取回流程
     *
     * @param taskId 当前任务ID
     * @param activityId  取回节点ID
     */
    public void callBackProcess(String taskId, String activityId);
    
    /**
     * 
     * @Description  根据任务ID获取对应的流程实例
     *
     * @param taskId 任务ID
     * @return ProcessInstance 流程实例
     */
    public ProcessInstance findProcessInstanceByTaskId(String taskId);
   
    /**
     * 
     * @Description 根据流程实例ID和任务key值查询所有同级任务集合
     *
     * @param processInstanceId 流程实例ID
     * @param definitionKey 流程定义ID
     * @return List<Task> 当前流程的任务集合
     */
    public List<Task> findTaskListByKey(String processInstanceId, String definitionKey);
 
    /**
     * 
     * @Description 根据流程定义id获取流程定义
     *
     * @param processDefinitionId 流程定义Id
     * @return ProcessDefinition 流程定义
     */
    public ProcessDefinition findProcessDefinitionById(String processDefinitionId);
	
   /**
    * 
    * @Description 
    *
    * @param taskId 当前任务ID
    * @param variables 流程变量
    * @param activityId 流程转向执行任务节点ID,此参数为空，默认为提交操作
    */
    public void commitProcess(String taskId, Map<String, Object> variables,String activityId);
    
	/**
	 * 
	 * @Description 还原指定活动节点流向
	 *
	 * @param activityImpl 活动节点
	 * @param oriPvmTransitionList 原有节点流向集合
	 */
    public void restoreTransition(ActivityImpl activityImpl,List<PvmTransition> oriPvmTransitionList) ;
	    
    /**
     * 
     * @Description  清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return List<PvmTransition> 节点流向集合
     */
    public List<PvmTransition> clearTransition(ActivityImpl activityImpl);
    
    public void turnTransition(String taskId, String activityId,Map<String, Object> variables); 
 	
    /**
     * 
     * @Description 删除流程定义
     *
     * @param delopmentId 流程定义id, 不能为空
     */
    public void deleteDeloyment(String deploymentId);
    
    /**
     * 
     * @Description 删除流程定义,包含关联表的全部删除
     *
     * @param deploymentId 流程定义id, 不能为空
     * @param flag  true 或者false
     */
	public void deleteDeloyment(String deploymentId,boolean flag);
 
	/**
	 * 
	 * @Description 删除所有的流程,不包含关联的删除
	 *
	 * @param delopmentId 流程定义id, 不能为空
	 */
	public void deleteAllDeloyment(String delopmentId);
	
	/**
	 * 
	 * @Description  删除所有的流程,包含关联的删除
	 *
	 * @param delopmentId 流程定义id, 不能为空
	 * @param flag  true 或者 是false
	 */
	public void deleteAllDeloyment(String delopmentId,boolean flag);

	/**
	 * 
	 * @Description  设置流程变量
	 *
	 * @param taskId  任务Id
	 * @param paramName 变量名称
	 * @param paramValue  变量值
	 */
	public void setVariables(String taskId,String paramName,String paramValue);
	
	/**
	 * 
	 * @Description  查询已经未结束流程的个人任务
	 *
	 * @param assignee 任务办理人 
	 * @return List<Task> 当前办理人未完结流程任务集合
	 */
	public List<HistoricTaskInstance> findPersonNotFinashedHistoryTask(String assignee );
	
	/**
	 * 
	 * @Description 查询已经结束流程的个人任务
	 *
	 * @param assignee 任务办理人
	 * @return List<Task> 办理人已经完结的流程任务集合
	 */
	public List<HistoricTaskInstance> findPersonFinashedHistoryTask(String assignee );
	
	/**
	 * 
	 * @Description 查看用户组任务
	 *
	 * @param candidateUser 办理人
	 * @return List<Task> 当前用户的组任务集合
	 */
	public List<Task> findGroupTask(String candidateUser);
	
	/**
	 * 
	 * @Description  获取流程变量
	 *
	 * @param executionId
	 * @param variableName 
	 * @return
	 */
	public Object findVariables(String executionId,String variableName );
	
	/**
	 * 
	 * @Description 获取到当前任务的图中位置信息
	 *
	 * @param taskId 任务的Id值
	 * @return 当前任务节点的位置信息
	 */
	public Map<String, String> getCurrentActivityCoordinates(String taskId);	
	
	public ActivityImpl getActivityImpl(RepositoryService repositoryService,String processDefId, String activityId);
	
	public ProcessDefinitionEntity getProcessDefinition(RepositoryService repositoryService, String processDefId);

	public ProcessDefinition findProcessDefinitionByProcessDefinitionId(String processDefinitionId);
	
	public void closeProcess(String processInstanceByKey,Map<String,Object> params);
	    
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId);
    
    public ActivityImpl findActivitiImpl(String taskId, String activityId);
    
	public Task findTaskByProcessInstanceId(String processInstanceId);
	
	public List<ActivityImpl> findBackAvtivity(String taskId);
	
	public TaskEntity findTaskById(String taskId);
    
    public void endProcess(String taskId);
    
    public List<Task> findHistoryTask();



}
