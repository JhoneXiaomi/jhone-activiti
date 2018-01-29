package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbCredentialMapper;
import com.hebabr.base.dao.TbUserVsCredentialMapper;
import com.hebabr.base.pojo.CredentialPojo;
import com.hebabr.base.service.ICredentialService;
import com.hebabr.model.TbUserVsCredential;
import com.hebabr.model.TbUserVsCredentialExample;
@Service
public class CredentialServiceImpl implements ICredentialService{
	@Autowired
	private TbCredentialMapper credentialMapper;
	@Autowired
	private TbUserVsCredentialMapper userVsCredentialMapper;
	
	@Override
	public List<CredentialPojo> listAllCredentialPojosByUserId(String userId) {
		return credentialMapper.listAllCredentialPojos(userId);
	}

	@Override
	public List<String> listCredentialIdListByUserId(String userId) {
		return userVsCredentialMapper.listCredentialIdListByUserId(userId);
	}

	@Override
	public void dropUserVsCredentialByUserId(String userId) {
		userVsCredentialMapper.dropUserVsCredentialByUserId(userId);
	}

	@Override
	public void insertUserVsCredential(TbUserVsCredential uvc) {
		userVsCredentialMapper.insert(uvc);
	}

}
