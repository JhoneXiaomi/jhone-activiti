package com.hebabr.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbUserEntrustMapper;
import com.hebabr.base.service.IEntrustService;
import com.hebabr.model.TbUserDto;
import com.hebabr.model.TbUserEntrust;
import com.hebabr.model.TbUserEntrustExample;
@Service
public class EntrustServiceImpl implements IEntrustService{
	@Autowired
	private TbUserEntrustMapper userEntrustMapper;
	
	@Override
	public void insertEntrust(TbUserEntrust entrust) {
		userEntrustMapper.insert(entrust);
	}

	@Override
	public TbUserEntrust getUserEntrustByuserIdAndDate(String userID, Date now) {
		List<TbUserEntrust> list=userEntrustMapper.getUserEntrustByuserIdAndDate(userID, now);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public TbUserEntrust getUserEntrustByid(String id) {
		return userEntrustMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateUserEntrust(TbUserEntrust entrust) {
		return userEntrustMapper.updateByPrimaryKey(entrust);
	}

	@Override
	public List<TbUserEntrust> listHisEntrustByUserIdAndDate(String userId, Date now) {
		return userEntrustMapper.listHisEntrustByUserIdAndDate(userId, now);
	}

	@Override
	public List<TbUserDto> ListCommissionedListByUserIdAndDate(String userId, Date now) {
		return userEntrustMapper.ListCommissionedListByUserIdAndDate(userId, now);
	}


}
