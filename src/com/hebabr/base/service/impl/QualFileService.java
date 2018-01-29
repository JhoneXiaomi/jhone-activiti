package com.hebabr.base.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbQualFileDtoMapper;
import com.hebabr.base.service.IQualFileService;
import com.hebabr.model.TbQualFileDto;
import com.hebabr.model.TbQualFileDtoExample;
import com.hebabr.model.TbQualFileDtoExample.Criteria;
@Service
public class QualFileService implements IQualFileService{

	
	@Autowired
	private TbQualFileDtoMapper tbQualFileDtoMapper;
	
	/**
	 * 
	 * 通过用户Id和资质Id查询对应的借阅文件列表
	 */
	@Override
	public List<TbQualFileDto> getQualFileListByUserIdAndQualId(String userId, String qualId) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("qualId", qualId);
		List<TbQualFileDto> qualFiles = tbQualFileDtoMapper.selectByUserIdAndQualId(map);
		return qualFiles;
	}

	/**
	 * 
	 * 添加审批文件
	 * @param qualFileDto 
	 * 				审批文件实体类
	 */
	@Override
	public boolean addQualFile(TbQualFileDto qualFileDto) {
		boolean flag = true;
		int result = tbQualFileDtoMapper.insert(qualFileDto);
		if(result==0)
			flag = false;
		return flag;
	}

	@Override
	public TbQualFileDto findQualFileByFileName(String fileName) {
		
		TbQualFileDtoExample tbQualFileDtoExample = new TbQualFileDtoExample();
		TbQualFileDto tbQualFileDto = null;
		Criteria tqc =  tbQualFileDtoExample.createCriteria();
		tqc.andQualFileNameEqualTo(fileName);
		List<TbQualFileDto> tbQualFileDtoList = tbQualFileDtoMapper.selectByExample(tbQualFileDtoExample);
		
		if(tbQualFileDtoList.size()>0)
			tbQualFileDto = tbQualFileDtoList.get(0);
		return tbQualFileDto;
	}

}
