package com.hebabr.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.service.IFlowProcessdefinitionService;
import com.hebabr.base.dao.TbUserDtoMapper;
import com.hebabr.base.dao.TbUserSignatureDtoMapper;
import com.hebabr.base.pojo.AssigneePojo;
import com.hebabr.base.pojo.NodeAssigneePojo;
import com.hebabr.base.pojo.UserHasOrgPojo;
import com.hebabr.base.pojo.UserListPojo;
import com.hebabr.base.pojo.UserOrgPojo;
import com.hebabr.base.service.IUserService;
import com.hebabr.base.util.Md5Util;
import com.hebabr.base.util.PropertiesUtils;
import com.hebabr.model.FlowNodeDto;
import com.hebabr.model.FlowProcessdefinitionDto;
import com.hebabr.model.TbUserDto;
import com.hebabr.model.TbUserDtoExample;
import com.hebabr.model.TbUserDtoExample.Criteria;
import com.hebabr.model.TbUserSignatureDto;
import com.hebabr.model.TbUserSignatureDtoExample;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private TbUserDtoMapper userMapper;

	@Autowired
	private TbUserSignatureDtoMapper userSignMapper;
	
	@Autowired
	private IFlowProcessdefinitionService iFlowProcessdefinitionService;
	
	@Autowired
	private IUserService userService;

	@Override
	public int insertUser(TbUserDto dto) {
		return userMapper.insert(dto);
	}

	@Override
	public TbUserDto queryUserById(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<UserListPojo> getAllUsersByCondition(TbUserDto user) {
		return userMapper.selectUserListByPrimaryKey(user);
	}

	@Override
	public TbUserDto getUserByLoginNameAndPasswd(String loginName, String passwd) {
		passwd = Md5Util.MD5Encode(passwd);
		TbUserDtoExample tude = new TbUserDtoExample();
		tude.createCriteria().andIsUsedEqualTo(true).andLoginNameEqualTo(loginName).andPasswdEqualTo(passwd);
		List<TbUserDto> userList = userMapper.selectByExample(tude);
		if (userList != null && userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void dropUserById(String id) {
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<TbUserDto> getAllUsersByOrgId(String orgId) {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		cc.andOrgIdEqualTo(orgId).andIsUsedEqualTo(true);
		return userMapper.selectByExample(tude);
	}

	@Override
	public TbUserDto getUserById(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateUser(TbUserDto user) {
		userMapper.updateByPrimaryKey(user);
	}

	@Override
	public TbUserDto getUserByOpenid(String openid) {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		cc.andWeixinNoEqualTo(openid);
		List<TbUserDto> userList = userMapper.selectByExample(tude);
		if (userList != null && userList.size() == 1) {
			return userList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public TbUserDto getUserByLoginName(String loginName) {
		if (loginName == null) {
			return null;
		} else {
			TbUserDtoExample tude = new TbUserDtoExample();
			Criteria cc = tude.createCriteria();
			cc.andLoginNameEqualTo(loginName);
			List<TbUserDto> users = userMapper.selectByExample(tude);
			if (users != null && users.size() > 0) {
				return users.get(0);
			} else {
				return null;
			}
		}

	}

	/**
	 * 
	 * 根据用户组获取所有的用户信息
	 */
	@Override
	public List<AssigneePojo> selectUserByGroupId(String groupId) {

		List<AssigneePojo> listUser = userMapper.selectUserByGroupId(groupId);
		List<AssigneePojo> userList = new LinkedList<AssigneePojo>();
		for (AssigneePojo assigneePojo : listUser) {
			assigneePojo.setChecked(false);
			userList.add(assigneePojo);
		}
		return userList;
	}

	@Override
	public List<AssigneePojo> selectUserByGroupIdAndNodeName(String groupId,String nodeName, String currentUserId,FlowNodeDto node) {
		String processTypeCode="";
		if(groupId==null){
			groupId=node.getUserGroupId();
		}
		if(nodeName==null){
			nodeName=node.getNodeParam();
		}
		if(node!=null){
			String processDefinitionId=node.getProcessdefineId();
			FlowProcessdefinitionDto fpd=iFlowProcessdefinitionService.findFlowProcessdefinitionByDevelopmentId(processDefinitionId);
			processTypeCode=fpd.getProcessTypeCode();
		}
		List<AssigneePojo> userList = new ArrayList<AssigneePojo>();
		List<AssigneePojo> listUser = new ArrayList<AssigneePojo>();
		if(!node.getIsAllPerson()){//选当前部门人员
			listUser = userMapper.selectUserByUserIdAndGroupId(currentUserId, groupId);
		}else{
			listUser = userService.selectUserByGroupId(groupId);
		}
		/*if ("director".equals(nodeName) || ("approvaler".equals(nodeName)&&!"2858f878055c11e79ba4".equals(processTypeCode))||"check".equals(nodeName)||("projectManager".equals(nodeName)&&"2858f878055c11e79ba4".equals(processTypeCode))) {// 所长,审批人 节点,不包括技术审批的审定
			listUser = userMapper.selectUserByUserIdAndGroupId(currentUserId, groupId);
		} else if ("printer".equals(nodeName)) {// 打印人节点：（9-11修改为本部门所有人）
			listUser = userService.selectOrgAssigneePojoListByUserId(currentUserId);
		}else if("progremManager".equals(nodeName)){//报告的项目负责人节点可选范围：本部门的所有人，项目负责人在前，其余在后（项目负责人判定条件为角色）
			listUser=userService.selectOrgAssigneePojoListByUserId(currentUserId);
		}else {
			listUser = userService.selectUserByGroupId(groupId);
		}*/
		for (AssigneePojo assigneePojo : listUser) {
			assigneePojo.setChecked(false);
			userList.add(assigneePojo);
		}
		return userList;
	}

	@Override
	public List<AssigneePojo> selectAllUsedUserByGroupId() {

		List<AssigneePojo> listUser = userMapper.selectAllUsedUserByGroupId();
		List<AssigneePojo> userList = new LinkedList<AssigneePojo>();
		for (AssigneePojo assigneePojo : listUser) {
			assigneePojo.setChecked(false);
			userList.add(assigneePojo);
		}
		return userList;
	}

	@Override
	public List<String> getAllUserIds() {
		return userMapper.getAllUserIds();
	}

	@Override
	public List<String> selectUsersByProcessInstanceIdAndActivityId(String processinstanceId, String activitityId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processInstance", processinstanceId);
		map.put("nodeId", activitityId);
		List<String> str = userMapper.selectUsersByProcessInstanceIdAndActivityId(map);
		return str;
	}

	/**
	 * 查询所有的用户信息
	 */
	@Override
	public List<TbUserDto> findAllUsers() {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		cc.andIsUsedEqualTo(true);
		List<TbUserDto> users = userMapper.selectByExample(tude);
		return users;
	}

	/**
	 * 获取所有的审批人的电子签名
	 */
	@Override
	public List<NodeAssigneePojo> findAllNodeAssigneeSignPics(String processInstanceId) {

		List<NodeAssigneePojo> NodeAssignees = userMapper.selectAllNodeAssigneeSignPics(processInstanceId);
		return NodeAssignees;
	}

	/**
	 * 
	 * 根据用户姓名获取用户的角色名称 create by zhangzhiwei 2017下午7:32:33
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> findRoleNameByUserId(String userId) {
		List<String> roleNames = userMapper.selectRoleNameByUserId(userId);
		return roleNames;
	}

	/**
	 * 
	 * 根据节点id 和 流程id 获取节点的签名 create by zhangzhiwei 2017/03/14 10:32:33
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public List<String> findUserSignPicsByActivitiIdAndProcessinstanceId(String activitiId, String processinstanceId) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activitiId", activitiId);
		map.put("processinstanceId", processinstanceId);
		List<String> listStr = userMapper.selectUserSignPics(map);
		return listStr;
	}

	@Override
	public List<TbUserDto> findByGroupId(String groupId) {
		List<TbUserDto> users = userMapper.selectByGroupId(groupId);
		return users;
	}

	@Override
	public List<TbUserDto> getAllUsers() {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		return userMapper.selectByExample(tude);
	}

	@Override
	public List<TbUserDto> findByQualId(String qualId) {
		List<TbUserDto> listUser = userMapper.selectByQualId(qualId);
		return listUser;
	}

	@Override
	public List<TbUserDto> getUsersUsed() {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		cc.andIsUsedEqualTo(true);
		return userMapper.selectByExample(tude);
	}

	// 搜索功能
	@Override
	public List<TbUserDto> getAllUsersBySearch(Map map) {
		TbUserDtoExample tude = new TbUserDtoExample();
		Criteria cc = tude.createCriteria();
		String cnName = (String) map.get("cnName");
		String phone = (String) map.get("phone");
		if (cnName.trim() != null) {
			cc.andCnNameEqualTo(cnName);
			return userMapper.selectByExample(tude);
		} else if (phone.trim() != null) {
			cc.andPhoneEqualTo(phone);
			return userMapper.selectByExample(tude);
		} else if (cnName.trim() != null && phone.trim() != null) {
			cc.andCnNameEqualTo(cnName);
			cc.andPhoneEqualTo(phone);
			return userMapper.selectByExample(tude);
		}

		return userMapper.selectByExample(tude);
	}

	@Override
	public TbUserSignatureDto getUserSignByUserId(String userId) {

		TbUserSignatureDtoExample tusde = new TbUserSignatureDtoExample();
		com.hebabr.model.TbUserSignatureDtoExample.Criteria cc = tusde.createCriteria();

		cc.andUserIdEqualTo(userId);
		List<TbUserSignatureDto> userSignList = userSignMapper.selectByExample(tusde);
		if (userSignList != null && userSignList.size() > 0) {
			return userSignList.get(0);
		}
		return null;
	}

	@Override
	public List<UserOrgPojo> getUserOrgPojosByUserIdAndOrgId(String userId, String orgId) {
		return userMapper.getUserOrgPojosByUserIdAndOrgId(userId, orgId);
	}

	@Override
	public List<UserOrgPojo> findUsersByNameOrPhone(String userId, String text) {

		return userMapper.findUserOrgPojoByNameOrPhone(userId, text);
	}

	@Override
	public List<UserOrgPojo> findUserOrgPojoByUserId(String userId) {
		return userMapper.findUserOrgPojoByUserId(userId);
	}

	// @黄 :此方法为了查询出带有部门的user用户,然后把这个用户重新封装到UserHasOrgPojo
	@Override
	public UserHasOrgPojo getUserHasOrgById(String userId) {
		UserHasOrgPojo uhop = userMapper.selectUserOrgByUserId(userId);
		return uhop;
	}

	@Override
	public List<UserOrgPojo> findAllUsedUserOrgPojo(String userId) {
		return userMapper.findAllUsedUserOrgPojo(userId);
	}

	@Override
	public List<TbUserDto> findAllProjectUsers() {
		//获取项目负责人ID
		String projectRoleNum=PropertiesUtils.getInstance().getConfigItem("projectRoleNum");
		return userMapper.selectAllProjectUsers(projectRoleNum);
	}

	@Override
	public List<TbUserDto> findProjectUsersByOrgId(String orgId) {
		//获取项目负责人ID
		//String projectRoleNum=PropertiesUtils.getInstance().getConfigItem("projectRoleNum");
		return userMapper.selectProjectUsersByOrgId(orgId);
	}

	@Override
	public List<UserListPojo> getUserListPojoList(Map map) {
		return userMapper.getUserListPojoList(map);
	}

	@Override
	public boolean checkCurrentUserIsProjectManager(String userId) {
		boolean flag = false;
		//获取项目负责人ID
		String projectRoleNum=PropertiesUtils.getInstance().getConfigItem("projectRoleNum");
		TbUserDto userDto = userMapper.selectProjectManagerByUserId(userId,projectRoleNum);
		if (null != userDto)
			flag = true;
		return flag;
	}

	@Override
	public List<TbUserDto> getUserGroupBygroupId(String groupId) {
		
		return userMapper.getUserGroupBygroupId(groupId);
	}

	@Override
	public List<TbUserDto> findAllLeaders() {
		return userMapper.selectAllLeaders();
	}

	@Override
	public List<TbUserDto> findProjectUsers() {
		//获取项目负责人ID
		String projectRoleNum=PropertiesUtils.getInstance().getConfigItem("projectRoleNum");
		return userMapper.findAllprojectUsers(projectRoleNum);
	}

	@Override
	public List<TbUserDto> getAllUserOrgs() {
		return userMapper.getAllUserOrgs();
	}

	@Override
	public List<TbUserDto> listLikelyUsers(String test) {
		return userMapper.listLikelyUsers(test);
	}

	@Override
	public List<TbUserDto> findSimilarUsersByQualTypeId(String qualTypeId) {

		return userMapper.selectSimilarUsersByQualTypeId(qualTypeId);
	}

	@Override
	public List<TbUserDto> findByProcessDefinitionIdAndNodeKeyId(String developmentId, String flowProcessTempId,
			String id) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("developmentId", developmentId);
		map.put("flowProcessTempId", flowProcessTempId);
		map.put("id", id);

		return userMapper.selectByProcessDefinitionIdAndNodeKeyId(map);
	}

	@Override
	public void saveUserSignature(TbUserSignatureDto userSign) {
		userSignMapper.insert(userSign);

	}

	@Override
	public TbUserSignatureDto getUserSignByCnName(String cnName) {
		TbUserSignatureDtoExample tusde = new TbUserSignatureDtoExample();
		com.hebabr.model.TbUserSignatureDtoExample.Criteria cc = tusde.createCriteria();
		cc.andSignNameEqualTo(cnName);
		List<TbUserSignatureDto> userSignList = userSignMapper.selectByExample(tusde);
		if (userSignList != null && userSignList.size() > 0) {
			return userSignList.get(0);
		}
		return null;
	}

	
	@Override
	public List<TbUserDto> findByQualTypeIdAndBelongNodeId(String qualTypeId, String belongNodeId) {
	
		return userMapper.selectByQualTypeIdAndBelongNodeId(qualTypeId,belongNodeId);
	}

	@Override
	public LinkedList<AssigneePojo> selectOrgAssigneePojoListByUserId(String userId) {
		LinkedList<AssigneePojo> unProgremManagers=userMapper.selectUnOrgProgremManagersByUserId(userId);
		LinkedList<AssigneePojo> progremManagers=userMapper.selectOrgProgremManagersByUserId(userId);
		progremManagers.addAll(unProgremManagers);
		return progremManagers;
	}
	
	

}
