package com.hebabr.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbLogDtoMapper;
import com.hebabr.base.pojo.RegistLogPojo;
import com.hebabr.base.service.ILogService;
import com.hebabr.model.TbLogDto;
import com.hebabr.model.TbLogDtoExample;

@Service
public class LogServiceImpl implements ILogService {
	
	@Autowired
    private TbLogDtoMapper logMapper;  

	@Override
	public int insertLog(TbLogDto dto) {
		// TODO Auto-generated method stub
		return logMapper.insert(dto);
	}

	@Override
	public List<TbLogDto> queryLogList(String logType) {
		// TODO Auto-generated method stub
		TbLogDtoExample lde = new TbLogDtoExample();
		lde.createCriteria().andLogTypeEqualTo(logType);  
		return logMapper.selectByExample(lde);
	}

	@Override
	public List<TbLogDto> getAllLoginAndLogOut(String type1, String type2) {
		return logMapper.getAllLoginAndLogOut(type1, type2);
	}

	@Override
	public List<TbLogDto> getAllExceptionLog() {
		return logMapper.getAllExceptionLog();
	}

	@Override
	public List<RegistLogPojo> getRegistLogPojoList(Map map) {
		return logMapper.getRegistLogPojoList(map);
	}

}
