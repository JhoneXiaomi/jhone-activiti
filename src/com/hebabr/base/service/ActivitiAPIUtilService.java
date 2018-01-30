package com.hebabr.base.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
 * 封装了activiti 常用的方法接口API
 * 
 * 
 * @author zhangzhiwei
 * 
 * @version	1.0.0
 */
public interface ActivitiAPIUtilService {

	public Deployment deployByBpmnAndImg(String delopmentName, String classForBpmn,String classForImg);
	
	public Deployment deployByZip(String delopmentName,String zipFile) throws FileNotFoundException;
	
	public ProcessInstance startProcessByKey(String processInstanceKey,Map<String,Object> params);
	
	public ProcessInstance startProcessById(String processinstanceDefinitionId,Map<String,Object> params);
	
	public boolean isCompleted(String processInstanceId);
	
    public List<ProcessDefinition> findProcessDefinition();
    
    public List<Task> findPersonTask(String assignee);
    
    public Task findTaskByTaskId(String taskid);
    
    public Task findTaskByProcessInstanceId(String processInstanceId);
    
    public Task findTaskByExecutionId(String executionId);
    
    public List<Task> findAllTask();
    
    public boolean findProcessState(String processInatanceId);
    
    public List<Task> findHistoryTask();

    public List<ActivityImpl> findBackAvtivity(String taskId);
   
    public ProcessInstance findProcessInstanceByTaskId(String taskId);
     
    public List<Task> findTaskListByKey(String processInstanceId, String key);
   
    public ActivityImpl findActivitiImpl(String taskId, String activityId);
  
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(  
             String taskId);
 
    public ProcessDefinition findProcessDefinitionById(  
             String processDefinitionId);
    
    public List<PvmTransition> clearTransition(ActivityImpl activityImpl);
    
    public TaskEntity findTaskById(String taskId);
    
    public void closeProcess(String processInstanceByKey,Map<String,Object> params);
	
    public void completePersonTask(String taskId, Map<String, Object> params);
	
    public void commitProcess(String taskId, Map<String, Object> variables,String activityId);
	
    public void restoreTransition(ActivityImpl activityImpl,List<PvmTransition> oriPvmTransitionList) ;
	    
    public void endProcess(String taskId);
    
    public void callBackProcess(String taskId, String activityId,Map<String, Object> params);
    
    public void callBackProcess(String taskId, String activityId);
    
    public void turnTransition(String taskId, String activityId,Map<String, Object> variables); 
 	
    public void deleteDeloyment(String delopmentId);
 
	public void deleteDeloyment(String delopmentId,boolean flag);
	
	public void deleteAllDeloyment(String delopmentId);
	
	public void deleteAllDeloyment(String delopmentId,boolean flag);

	public void setVariables(String taskId,String paramName,String paramValue);
	
	public void readProcessDefindNodeFromLocahost(HttpSession session,String filePath);
	
	public void readProcessDefindNodeFromNetwork(String userId,String processDefinitionId);
	
	public boolean startProcessInstance(HttpSession session,String flowProcessinstanceKeyId,String processinstanceDefinitionId,Map<String,Object> params,String title);
	public List<HistoricTaskInstance> findPersonNotFinashedHistoryTask(String assignee );
	
	public List<HistoricTaskInstance> findPersonFinashedHistoryTask(String assignee );
	
	
	public List<Task> findGroupTask(String assigneeId);
	
	
	public Object findVariables(String processInstanceId,String paramName);
	
	public Map<String, String> getCurrentActivityCoordinates(String taskId);	
	
	public Map<String,Object> startNodeParamInit(String processDefinitionId);
	
	public ActivityImpl getActivityImpl(RepositoryService repositoryService,String processDefId, String activityId);
	
	public ProcessDefinitionEntity getProcessDefinition(RepositoryService repositoryService, String processDefId);

	public ProcessDefinition findProcessDefinitionByProcessDefinitionId(String processDefinitionId);

}
