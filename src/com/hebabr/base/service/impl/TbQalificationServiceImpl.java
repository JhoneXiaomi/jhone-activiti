package com.hebabr.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbQualFileDtoMapper;
import com.hebabr.base.dao.TbQualificationMapper;
import com.hebabr.base.pojo.QualificationAndUserPojo;
import com.hebabr.base.service.ITbQalificationService;
import com.hebabr.model.TbQualFileDto;
import com.hebabr.model.TbQualification;
import com.hebabr.model.TbQualificationExample;
import com.hebabr.model.TbQualificationExample.Criteria;
@Service
public class TbQalificationServiceImpl implements ITbQalificationService{

	
	@Autowired
	private TbQualificationMapper tbQualificationMapper;
	
	@Autowired
	private TbQualFileDtoMapper qualFileMapper;
	
	/**
	 * 
	 * 选择所有的证书列表
	 */
	@Override
	public List<TbQualification> findAllQalification() {
		List<TbQualification> listTbQualification = tbQualificationMapper.selectAllQualifications();
		return listTbQualification ;
	}
	/**
	 * 
	 * 根据组织机构Id获取对应的资质信息
	 */
	
	@Override
	public List<TbQualification> findQalificationsByOrgId(String typeId) {
		TbQualificationExample tode = new TbQualificationExample();
		Criteria cc = tode.createCriteria(); 
		cc.andOrgIdEqualTo(typeId).andIsUsedEqualTo(true);
		return tbQualificationMapper.selectByExample(tode);
		/*List<TbQualification> qualifications = tbQualificationMapper.selectQualificationsByOrgId(OrgId);
		return qualifications;*/
	}
	// 根据资
	@Override
	public TbQualification findQalificationsById(String qalificationId) {
		
		TbQualification tbQualification = tbQualificationMapper.selectByPrimaryKey(qalificationId);
		return tbQualification;
	}
	@Override
	public int insertQalification(TbQualification qual) {
		return tbQualificationMapper.insert(qual);
	}
	@Override
	public int insertQualFile(TbQualFileDto qualFile) {
		return qualFileMapper.insert(qualFile);
	}
	@Override
	public List<QualificationAndUserPojo> findAllQualAndUser(Map map) {
		return tbQualificationMapper.findAllQualAndUser(map);
	}
	@Override
	public TbQualification getQualById(String id) {
		return tbQualificationMapper.selectByPrimaryKey(id);
	}
	@Override
	public int updateQualification(TbQualification qual) {
		return tbQualificationMapper.updateByPrimaryKey(qual);
	}
	@Override
	public int dropQualficationById(String id) {
		return tbQualificationMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int dropQualFileByQualId(String qualId) {
		return qualFileMapper.deleteByQualId(qualId);
	}
	@Override
	public List<TbQualFileDto> findQualFileByQualId(String qualId) {
		return qualFileMapper.findQualFileByQualIdAndOrderBySortNo(qualId);
	}
	@Override
	public void dropQualFileById(String fileId) {
		qualFileMapper.deleteByPrimaryKey(fileId);
	}
	
	

}
