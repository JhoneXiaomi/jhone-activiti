package com.hebabr.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.approval.service.IFlowProcessFileService;
import com.hebabr.base.dao.TbYunFileDtoMapper;
import com.hebabr.base.service.TbYunFileSevieve;
import com.hebabr.mine.service.IUserFileService;
import com.hebabr.model.FlowProcessFileDto;
import com.hebabr.model.TbYunFileDto;

@Service
public class TbYunFileSevieveImpl implements TbYunFileSevieve {

	@Autowired
	private TbYunFileDtoMapper tbYunFileDtoMapper;
	@Autowired
	private IUserFileService iUserFileService;
	@Autowired
	private IFlowProcessFileService iFlowProcessFileService;

	/**
	 * 
	 * 查寻出对应的云盘文件信息
	 */
	@Override
	public TbYunFileDto findYunFileById(String id) {

		TbYunFileDto tbYunFileDto = tbYunFileDtoMapper.selectByPrimaryKey(id);
		return tbYunFileDto;
	}

	@Override
	public boolean deleteById(String id) {
		boolean flag = false;
		Integer result = tbYunFileDtoMapper.deleteByPrimaryKey(id);
		if (result > 0)
			flag = true;
		return flag;
	}

	@Override
	public String[] updateFlowProcessFile(String yunFileId, String flowId, String flowProcessinstanceKeyId,
			String userId, String nodeId, String flowTempId) {
		yunFileId = yunFileId.replaceAll("null", "");
		if (yunFileId.endsWith("|"))
			yunFileId = yunFileId.substring(0, yunFileId.length() - 1);
		String[] yunFileIdAry = yunFileId.split("\\|");
		return updateFlowProcessFile(yunFileIdAry, flowId, flowProcessinstanceKeyId, userId, nodeId, flowTempId);

	}

	@Override
	public String[] updateFlowProcessFile(String[] yunFileIdAry, String flowId, String flowProcessinstanceKeyId,
			String userId, String nodeId, String flowTempId) {
		 
		if (null == yunFileIdAry) 
			yunFileIdAry = new String[]{""};
		
		if (nodeId == null)
			nodeId = "";
		String[] resultCount = new String[yunFileIdAry.length];
		// 查询已经保存的附件集合。
		List<FlowProcessFileDto> list = iFlowProcessFileService.findByFlowTempId(flowTempId, nodeId, userId);
		int count = list.size();
		List<String> fileIdList = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			fileIdList.add(list.get(i).getId());
		}

		for (int i = 0; i < yunFileIdAry.length; i++) {
			if (fileIdList.contains(yunFileIdAry[i])) {
				fileIdList.remove(yunFileIdAry[i]);
				resultCount[i] = yunFileIdAry[i];
			} else if (StringUtils.isNotBlank(yunFileIdAry[i])) {
				// 保存到流程文件表单中
				FlowProcessFileDto dto = iUserFileService.saveFlowFileByYunFileId(yunFileIdAry[i], flowId,
						flowProcessinstanceKeyId, userId, nodeId, flowTempId);
				resultCount[i] = dto.getId();
			} else {

			}
		}

		// 移除删除的文件信息。
		for (int j = 0; j < fileIdList.size(); j++) {
			iFlowProcessFileService.disabledProcessFileByKeyId(fileIdList.get(j));
		}
		return resultCount;
	}

	@Override
	public int updateFlowProcessFileNodeId(String flowTempId, String processInstanceId, String nodeId, String userId) {
		int res = iFlowProcessFileService.updateNodeIdByFlowTempId(flowTempId, processInstanceId, nodeId, userId);
		return res;
	}


	@Override
	public String[] saveFlowProcessFiles(String[] yunFileIdAry, String processInstanceId, String processInstanceKeyId,
			String userId, String nodeId, String flowTempId) {

		if (nodeId == null)
			nodeId = "";
		String[] resultCount = new String[yunFileIdAry.length];
		// 查询已经保存的附件集合。
		List<FlowProcessFileDto> list = iFlowProcessFileService.findByFlowTempId(flowTempId, nodeId, userId);
		int count = list.size();
		List<String> fileIdList = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			fileIdList.add(list.get(i).getId());
		}

		for (int i = 0; i < yunFileIdAry.length; i++) {
			if (fileIdList.contains(yunFileIdAry[i])) {
				fileIdList.remove(yunFileIdAry[i]);
				resultCount[i] = yunFileIdAry[i];
			} else if (StringUtils.isNotBlank(yunFileIdAry[i])) {
				// 保存到流程文件表单中
				FlowProcessFileDto dto = iUserFileService.saveFlowFileByYunFileId(yunFileIdAry[i], processInstanceId,
						processInstanceKeyId, userId, nodeId, flowTempId);
				resultCount[i] = dto.getId();
			} else {

			}
		}
		return resultCount;
	}

	@Override
	public String[] saveFlowProcessFiles(String yunFileId, String flowId, String flowProcessinstanceKeyId,
			String userId, String nodeId, String flowTempId) {
		yunFileId = yunFileId.replaceAll("null", "");
		if (yunFileId.endsWith("|"))
			yunFileId = yunFileId.substring(0, yunFileId.length() - 1);
		String[] yunFileIdAry = yunFileId.split("\\|");
		String[] resultCount = new String[yunFileIdAry.length];
		for (int i = 0; i < yunFileIdAry.length; i++) {
			if (StringUtils.isNotBlank(yunFileIdAry[i])) {
				// 保存到流程文件表单中
				FlowProcessFileDto dto = iUserFileService.saveFlowFileByYunFileId(yunFileIdAry[i], flowId,
						flowProcessinstanceKeyId, userId, nodeId, flowTempId);
				resultCount[i] = dto.getId();
			} 
		}
		return resultCount;
	}


}
