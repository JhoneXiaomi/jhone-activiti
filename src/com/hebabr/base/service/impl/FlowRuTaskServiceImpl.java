package com.hebabr.base.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.service.IFlowLogService;
import com.hebabr.approval.service.IFlowNodeAssigneeService;
import com.hebabr.approval.service.IFlowNodeService;
import com.hebabr.approval.service.IFlowProcessinstanceService;
import com.hebabr.base.dao.FlowRuTaskDtoMapper;
import com.hebabr.base.service.ActivitiAPIUtilService;
import com.hebabr.base.service.IFlowRuTaskService;
import com.hebabr.base.service.IUserService;
import com.hebabr.mine.service.IMessageService;
import com.hebabr.model.FlowNodeAssigneeDto;
import com.hebabr.model.FlowNodeDto;
import com.hebabr.model.FlowNodeMessageDto;
import com.hebabr.model.FlowProcessinstanceDto;
import com.hebabr.model.FlowRuTaskDto;
import com.hebabr.model.TbUserDto;

/**
 * 流程正在执行的任务实现类
 * 
 * @author zhangzhiwei 2017/04/09 AM 07/51
 *
 */
@Service
public class FlowRuTaskServiceImpl implements IFlowRuTaskService {

	@Autowired
	private FlowRuTaskDtoMapper flowRuTaskDtoMapper;
	@Autowired
	private ActivitiAPIUtilService activitiAPIUtilService;
	@Autowired
	private IFlowNodeService iflowNodeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IFlowNodeAssigneeService iFlowNodeAssigneeService;
	@Autowired
	private IFlowRuTaskService iFlowRuTaskService;
	@Autowired
	private IMessageService messageService;
	@Autowired
	private IFlowProcessinstanceService iflowProcessinstanceService;
	@Autowired
	private IFlowLogService iFlowLogService;

	private String nextNodeId;

	@Override
	public String completeProcessInstanceTask(List<String> acceptPerson, String processInstanceId, TbUserDto user,
			final String personTaskId, String currentNodeId, String nodeMessageId, String processDefinitionId,
			String flowNodeId, Integer flowState, String messageFlowName, Boolean isSimilarProject) {
		FlowProcessinstanceDto flowProcessinstanceDto=iflowProcessinstanceService.findByProcessInstanceId(processInstanceId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != isSimilarProject) 
			map.put("isSimilarProject", isSimilarProject);
		String nextTaskId = new String(personTaskId);
		// 更新审批人的审批状态为
		iFlowNodeAssigneeService.updateAssignee(4, 1, nodeMessageId, flowNodeId, processInstanceId, user.getId());
		if (flowState == 1) {// 顺发
			// 完成自定义任务， 根据流程Id,节点ID,用户id删除任务信息
			iFlowRuTaskService.deleteByPiIdAndNodeIdAndUserId(processInstanceId, currentNodeId, user.getId());
			// 获取当前顺发节点下一个审批人信息。
			FlowNodeAssigneeDto nextAssignee = iFlowNodeAssigneeService
					.findNextByPiIdAndAndActivityIdAndUserId(processInstanceId, currentNodeId, user.getId());

			// 判断下一个审批人是否存在，存在则添加任务，不存在则完成当前工作流任务。
			if (null != nextAssignee) {
				// 添加当前节点下一审批人信息。
				addTaskToNextAssignee(nextAssignee.getUserId(), user.getId(), currentNodeId, processDefinitionId,
						processInstanceId, 1, personTaskId);

				// 更新下一个接受人的接收时间
				iFlowNodeAssigneeService.updateNodeAssigneeByProcessInstanceAndFlowNodeIdAndUserId(
						nextAssignee.getUserId(), processInstanceId, flowNodeId, user.getId(), new Date(), new Date());
				// 接收人的信息
				TbUserDto userDto = userService.getUserById(nextAssignee.getUserId());
				acceptPerson.add(userDto.getCnName());
				messageService.saveMessageInfo(processInstanceId, messageFlowName, user.getId(), user.getCnName(),
						userDto.getId(), 1, "flow2");
			} else {
				// 完成工作流任务。推送到下一个任务节点。
				activitiAPIUtilService.completePersonTask(personTaskId, map);
				nextTaskId = getTaskId(processInstanceId);
				if (StringUtils.isNotBlank(nextTaskId)) {
					List<String> person = addTaskToNextAssignee(nextTaskId, processInstanceId, processDefinitionId,
							user.getId(), messageFlowName, user.getCnName());
					acceptPerson.addAll(person);
				}
			}
		} else if (flowState == 2) {// 并发

			// 获取当前节点的任务
			List<FlowRuTaskDto> listRuTask = iFlowRuTaskService.findByPiIdAndNodeId(processInstanceId, currentNodeId);

			if (listRuTask.size() == 1) {
				// 完成工作流任务。推送到下一个任务节点。
				activitiAPIUtilService.completePersonTask(personTaskId, map);
				// 根据任务Id删除自定义任务表中任务
				iFlowRuTaskService.deletByTaskId(personTaskId);
				nextTaskId = getTaskId(processInstanceId);
				if (StringUtils.isNotBlank(nextTaskId)) {
					List<String> person = addTaskToNextAssignee(nextTaskId, processInstanceId, processDefinitionId,
							user.getId(), messageFlowName, user.getCnName());
					acceptPerson.addAll(person);
				}
			} else if (listRuTask.size() > 1) {
				// 完成自定义任务， 根据流程Id,节点ID,用户id删除任务信息
				iFlowRuTaskService.deleteByPiIdAndNodeIdAndUserId(processInstanceId, currentNodeId, user.getId());

				List<FlowNodeAssigneeDto> listAssignees = iFlowNodeAssigneeService
						.findByActivityIdAndProcessinstanceId(currentNodeId, processInstanceId);
				// 获取当前节点的审批人
				for (FlowNodeAssigneeDto as : listAssignees) {
					TbUserDto userDto = userService.getUserById(as.getUserId());
					acceptPerson.add(userDto.getCnName());
				}
			}
		} else if (flowState == 3) {// 抢发
			activitiAPIUtilService.completePersonTask(personTaskId, map);
			// 删除自定义任务表中任务
			iFlowRuTaskService.deletByTaskId(personTaskId);
			nextTaskId = getTaskId(processInstanceId);
			if (StringUtils.isNotBlank(nextTaskId)) {
				List<String> person = addTaskToNextAssignee(nextTaskId, processInstanceId, processDefinitionId,
						user.getId(), messageFlowName, user.getCnName());
				acceptPerson.addAll(person);
			}
		}
		messageService.saveMessageInfo(processInstanceId, messageFlowName, user.getId(), user.getCnName(),
				flowProcessinstanceDto.getCreateId(), 1, "flow1");

		return nextTaskId;
	}

	@Override
	public boolean add(FlowRuTaskDto flowRuTaskDto) {
		boolean flag = false;
		int result = flowRuTaskDtoMapper.insertSelective(flowRuTaskDto);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public boolean deletById(String primaryId) {
		boolean flag = false;
		int result = flowRuTaskDtoMapper.deleteByPrimaryKey(primaryId);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public boolean deletByTaskId(String taskId) {
		boolean flag = false;
		int result = flowRuTaskDtoMapper.deleteByTaskId(taskId);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public FlowRuTaskDto findByPiIdAndNodeIdAndUserId(String processInstanceId, String nodeId, String userId) {
		return null;
	}

	@Override
	public boolean deleteByPiIdAndNodeIdAndUserId(String processInstanceId, String activityId, String userId) {
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processInstanceId", processInstanceId);
		map.put("activityId", activityId);
		map.put("userId", userId);
		int result = flowRuTaskDtoMapper.deleteByPiIdAndNodeIdAndUserId(map);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public List<FlowRuTaskDto> findByPiIdAndNodeId(String processInstanceId, String nodeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processInstanceId", processInstanceId);
		map.put("nodeId", nodeId);
		List<FlowRuTaskDto> listTasks = flowRuTaskDtoMapper.selectByPiIdAndNodeId(map);
		return listTasks;
	}

	@Override
	public boolean deleteOtherAssigneeByPiIdAndNodeIdAndUserId(String processInstanceId, String activityId,
			String userId) {

		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processInstanceId", processInstanceId);
		map.put("activityId", activityId);
		map.put("userId", userId);
		int result = flowRuTaskDtoMapper.deleteOtherAssigneeByPiIdAndNodeIdAndUserId(map);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public boolean deleteByProcessInstanceId(String processInstanceId) {
		boolean flag = false;
		int result = flowRuTaskDtoMapper.deleteByProcessInstanceId(processInstanceId);
		if (result > 0)
			flag = true;
		return flag;
	}

	public String getTaskId(String processInstanceId) {
		// 获取当前的任务
		String taskId = null;
		Task newTask = activitiAPIUtilService.findTaskByExecutionId(processInstanceId);
		if (newTask != null) {
			taskId = newTask.getId();
		}
		return taskId;
	}

	@Override
	public List<String> addTaskToNextAssignee(String taskId, String processInstanceId, String processDefinitionId,
			String userId, String messageFlowName, String messageInitiatorName) {
		List<String> acceptPersons = new LinkedList<String>();

		String nodeId = activitiAPIUtilService.findTaskById(taskId).getTaskDefinitionKey();

		FlowNodeDto newFlowNode = iflowNodeService.findByNodeIdAndProcessDefinitionId(nodeId, processDefinitionId);
		Integer asssigneeType = newFlowNode.getNodeAssigneeType();
		if (asssigneeType == 1) {// 顺发

			FlowNodeAssigneeDto firstAssignee = iFlowNodeAssigneeService
					.findCurrentNodeFirstByPiIdAndActivityId(processInstanceId, nodeId);

			addTaskToNextAssignee(firstAssignee.getUserId(), userId, nodeId, processDefinitionId, processInstanceId,
					asssigneeType, taskId);

			TbUserDto userDto = userService.getUserById(firstAssignee.getUserId());// 保存接收人的信息
			acceptPersons.add(userDto.getCnName());
			// 保存消息
			messageService.saveMessageInfo(processInstanceId, messageFlowName, userId, messageInitiatorName,
					userDto.getId(), 1, "flow2");
		} else if (asssigneeType == 2 || asssigneeType == 3) { // 2 并发 3 抢发
			List<FlowNodeAssigneeDto> assigneeList = iFlowNodeAssigneeService
					.findByActivityIdAndProcessinstanceId(nodeId, processInstanceId);
			for (FlowNodeAssigneeDto as : assigneeList) {
				addTaskToNextAssignee(as.getUserId(), userId, nodeId, processDefinitionId, as.getProcessinstanceId(),
						asssigneeType, taskId);

				TbUserDto userDto = userService.getUserById(as.getUserId());// 保存接收人的信息
				acceptPersons.add(userDto.getCnName());
				// 保存消息
				messageService.saveMessageInfo(processInstanceId, messageFlowName, userId, messageInitiatorName,
						userDto.getId(), 1, "flow2");
			}
		}
		return acceptPersons;
	}

	public boolean addTaskToNextAssignee(String AssigneeId, String userId, String nodeId, String processDefinitionId,
			String processinstanceId, Integer asssigneeType, String taskId) {

		// 更新下一个审批人的接收时间

		FlowRuTaskDto flowRuTaskDto = new FlowRuTaskDto();
		flowRuTaskDto.setAssigneeId(AssigneeId);
		flowRuTaskDto.setCreateId(userId);
		flowRuTaskDto.setCreateTime(new Date());
		flowRuTaskDto.setNodeId(nodeId);
		flowRuTaskDto.setProcessDefinitionId(processDefinitionId);
		flowRuTaskDto.setProcessInstanceId(processinstanceId);
		flowRuTaskDto.setState(asssigneeType);
		flowRuTaskDto.setTaskId(taskId);
		return iFlowRuTaskService.add(flowRuTaskDto);
	}

	@Override
	public List<String> taskPass(FlowProcessinstanceDto fpd, TaskEntity task, String nodeMessageId,
			FlowNodeMessageDto flowNodeMessageDto, FlowNodeDto flowNodeDto, TbUserDto user, Boolean isSimilarProject) {

		String processInstanceId = fpd.getProcessinstanceId();
		String processDefinitionId = fpd.getProcessdefintionId();
		String currentNodeId = task.getTaskDefinitionKey();

		// 获取当前节点审批状态
		FlowNodeAssigneeDto fnad = iFlowNodeAssigneeService.findByNodeIdAndFiIdAndUserId(currentNodeId,
				processInstanceId, user.getId());

		if (fnad.getState() == 6) {
			// 更新驳回到审批人之后节点的审批状态为未读。
			iFlowNodeAssigneeService.updateCurrentUserAfterStateByUserIdAndPiIdAndActivityId(user.getId(),
					processInstanceId, currentNodeId);
			if (flowNodeDto.getNodeAssigneeType() == 2) {
				// 获取当前节点的审批人
				List<FlowNodeAssigneeDto> listNodeAssignees = iFlowNodeAssigneeService
						.findByActivityIdAndProcessinstanceId(flowNodeDto.getNodeId(), processInstanceId);
				for (FlowNodeAssigneeDto node : listNodeAssignees) {
					addTaskToNextAssignee(node.getUserId(), user.getId(),
							iflowNodeService.findNodesById(node.getFlowNodeId()).getNodeId(), processDefinitionId,
							processInstanceId, flowNodeDto.getNodeAssigneeType(), task.getId());
					// 更新接收人的接收時間
					iFlowNodeAssigneeService.updateNodeAssigneeByProcessInstanceAndFlowNodeIdAndUserId(node.getUserId(),
							processInstanceId, flowNodeDto.getId(), user.getId(), new Date(), new Date());
				}

			} else {
				// 添加驳回删除的任务。
				addTaskToNextAssignee(user.getId(), user.getId(), currentNodeId, processDefinitionId, processInstanceId,
						flowNodeDto.getNodeAssigneeType(), task.getId());
			}
		}
		List<String> acceptPersons = new LinkedList<String>();
		// 完成当前任务办理
		String personTaskId = completeProcessInstanceTask(acceptPersons, processInstanceId, user, task.getId(),
				currentNodeId, nodeMessageId, processDefinitionId, flowNodeDto.getId(),
				flowNodeDto.getNodeAssigneeType(), fpd.getName(),isSimilarProject);

		String nodeId = "";
		if (StringUtils.isNotEmpty(personTaskId)) {
			// 更新流程信息
			iflowProcessinstanceService.updateFlowProcessInstance(fpd, "", null, processInstanceId, null,
					user.getId(), fpd.getName(), 3);
			if (!personTaskId.equals(task.getId())) {
				// 获取节点Id
				nodeId = activitiAPIUtilService.findTaskById(personTaskId).getTaskDefinitionKey();
				// 设置下一节点Id用于返回。
				this.setNextNodeId(nodeId);

				// 更新下一个审批节点审批信息
				List<FlowNodeAssigneeDto> listNodeAssignees = iFlowNodeAssigneeService
						.findByActivityIdAndProcessinstanceId(nodeId, processInstanceId);
				if (listNodeAssignees.size() > 0)
					for (FlowNodeAssigneeDto nodeAssignee : listNodeAssignees) {
						iFlowNodeAssigneeService.updateAssignee(nodeAssignee, 2, 2);
					}
			}
		} else if (StringUtils.isEmpty(personTaskId)) {
			iflowProcessinstanceService.updateFlowProcessInstance(fpd, "endTime", currentNodeId, processInstanceId,
					null, user.getId(), fpd.getName(), 5);// 更新
		}

		return acceptPersons;
	}

	@Override
	public List<String> taskReject(FlowProcessinstanceDto fpd, Task task, String nodeMessageId, String rejectNodeId,
			String rejectUserId, String flowNodeId, TbUserDto user) {

		String processInstanceId = fpd.getProcessinstanceId();
		// 驳回流程 驳回当前任务
		activitiAPIUtilService.callBackProcess(task.getId(), rejectNodeId, null);
		// 更新当前审批人的状态为已审批
		iFlowNodeAssigneeService.updateAssignee(5, 1, nodeMessageId, flowNodeId, processInstanceId, user.getId());
		// 更新驳回至的节点的状态
		iFlowNodeAssigneeService.updateByUserIdAndProcessInstanceIdAndActivityId(7, rejectUserId, processInstanceId,
				rejectNodeId);
		iflowProcessinstanceService.updateFlowProcessInstance(fpd, "", "", "", null, user.getId(), "", 4);
		// 根据流程ID删除所有的当前待办的任务
		iFlowRuTaskService.deleteByProcessInstanceId(processInstanceId);

		return null;
	}

	@Override
	public List<String> taskSubmit(Task persontask, String currentNodeId, String title, String[] nodeFlowfileIds,
			TbUserDto user) {

		return taskSubmit(persontask, currentNodeId, title, nodeFlowfileIds, user, null);
	}

	@Override
	public List<String> taskSubmit(Task persontask, String currentNodeId, String title, String[] nodeFlowfileIds,
			TbUserDto user, Map<String, Object> assignees) {

		String takId = persontask.getId();
		String processInstanceId = persontask.getProcessInstanceId();
		String processdefintionId = persontask.getProcessDefinitionId();

		FlowNodeDto flowNodeDto = iflowNodeService.findByNodeIdAndProcessDefinitionId(currentNodeId,
				processdefintionId);

		// 完成当前任务
		activitiAPIUtilService.completePersonTask(takId, assignees);
		// 更新发起人的审批信息
		iFlowNodeAssigneeService.updateAssignee(1, 1, "", flowNodeDto.getId(), processInstanceId, user.getId());
		// 变更审批人之后的审批人状态为未读
		iFlowNodeAssigneeService.updateCurrentUserAfterStateByUserIdAndPiIdAndActivityId(user.getId(),
				processInstanceId, currentNodeId);
		iFlowRuTaskService.deleteByProcessInstanceId(processInstanceId); // 删除自定义任务表中任务

		// 根据流程ID获取新开启的任务
		Task newTask = activitiAPIUtilService.findTaskByExecutionId(processInstanceId); // 根据流程ID获取新开启的任务
		List<String> acceptPersons = new LinkedList<String>();
		String nodeId = "";
		if (null != newTask) {
			nodeId = activitiAPIUtilService.findTaskById(newTask.getId()).getTaskDefinitionKey();
			// 为下一个节点添加任务
			acceptPersons = addTaskToNextAssignee(newTask.getId(), processInstanceId, processdefintionId, user.getId(),
					title, user.getCnName());
			// 获取下一个审批节点审批人信息，更新审批人信息为未读。
			List<FlowNodeAssigneeDto> listNodeAssignees = iFlowNodeAssigneeService
					.findByActivityIdAndProcessinstanceId(nodeId, processInstanceId);
			if (listNodeAssignees.size() > 0)
				for (FlowNodeAssigneeDto nodeAssignee : listNodeAssignees) {
					iFlowNodeAssigneeService.updateAssignee(nodeAssignee, 2, 2);
				}
		}
		// 添加日志记录
		iFlowLogService.saveEachStepProcessInstanceLog(user, acceptPersons, "", currentNodeId, nodeId,
				flowNodeDto.getId(), String.join(",", nodeFlowfileIds), processInstanceId, processdefintionId, 1, 2);

		return acceptPersons;

	}

	@Override
	public String getNextNodeId() {
		return nextNodeId;
	}

	private void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}

}
