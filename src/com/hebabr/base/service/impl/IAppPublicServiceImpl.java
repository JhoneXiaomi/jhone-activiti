package com.hebabr.base.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.app.pojo.NodePojo;
import com.hebabr.app.pojo.base.AppQualTypePojo;
import com.hebabr.approval.pojo.DraftTenderDocPojo;
import com.hebabr.base.service.IAppPublicService;
import com.hebabr.base.service.ITbDictionaryService;
import com.hebabr.base.service.ITbQualTypeService;
import com.hebabr.model.TbQualType;


@Service
public class IAppPublicServiceImpl implements IAppPublicService{

	@Autowired
	private ITbQualTypeService iTbQualTypeService;
	@Autowired
    private ITbDictionaryService iTbDictionaryService;
	
	@Override
	public AppQualTypePojo findQualTypePojo(String qualTypeId,String qualTypeSelected) {
		AppQualTypePojo appQualTypePojo = new AppQualTypePojo();
		List<TbQualType> types = iTbQualTypeService.findQualTypesByParentId(qualTypeId);
		appQualTypePojo.setIndex(0);
		for (int i = 0;i< types.size();i++) {
		    TbQualType qualType = types.get(i);
			appQualTypePojo.getTypeQualListNames().add(qualType.getQualTypeName());
			appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(qualType.getId()));
			if (qualTypeSelected.equals(types.get(i).getId()))
					appQualTypePojo.setIndex(i);
		}
		appQualTypePojo.setTypeQualList(types);
		return appQualTypePojo;
	}
	/*@Override
	public AppQualTypePojo findQualTypePojo(String qualTypeId,String processDefinitionId,DraftTenderDocPojo tenderDocPojo) {

		AppQualTypePojo appQualTypePojo = new AppQualTypePojo();
		List<TbQualType> types = iTbQualTypeService.findQualTypesByParentId(qualTypeId);
		appQualTypePojo.setIndex(0);
		for (int i = 0;i< types.size();i++) {
		    TbQualType qualType = types.get(i);
			appQualTypePojo.getTypeQualListNames().add(qualType.getQualTypeName());
			appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(qualType.getId()));
			if (qualTypeId.equals(types.get(i).getId()))
					appQualTypePojo.setIndex(i);
		}
		appQualTypePojo.setTypeQualList(types);*/
		// 初始化资质类别
		/*if (qualTypeId.equals("12345678901234567890123456789010")) {//其他
			
			List<TbQualType> types = iTbQualTypeService.findQualTypesByParentId(qualTypeId);
			appQualTypePojo.setIndex(0);
			for (int i = 0;i< types.size();i++) {
			    TbQualType qualType = types.get(i);
				appQualTypePojo.getTypeQualListNames().add(qualType.getQualTypeName());
				appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(qualType.getId()));
				if (qualTypeId.equals(types.get(i).getId()))
						appQualTypePojo.setIndex(i);
			}
			appQualTypePojo.setTypeQualList(types);
		} else if (qualTypeId.equals("root")){//河北建研科技有限公司
			List<TbQualType> types=new LinkedList<TbQualType>();
			TbQualType groundWork=new TbQualType();
			groundWork.setId("12345678901234567890123456789002");
			groundWork.setQualTypeName("河北建筑科技有限公司地上施工类");
			types.add(groundWork);
			TbQualType underGroundWork=new TbQualType();
			underGroundWork.setId("12345678901234567890123456789003");
			underGroundWork.setQualTypeName("河北建筑科技有限公司地下施工类");
			types.add(underGroundWork);
			for (int i = 0;i< types.size();i++) {
				appQualTypePojo.getTypeQualListNames().add(types.get(i).getQualTypeName());
				if (null != tenderDocPojo) {
					if (tenderDocPojo.getQualTypeId().equals(types.get(i).getId()))
							appQualTypePojo.setIndex(i);
				} else 
					appQualTypePojo.setIndex(0);
			}
			appQualTypePojo.setTypeQualList(types);
		} else {
			TbQualType tqt = iTbQualTypeService.findByProcessDefinitionId(processDefinitionId);
			List<TbQualType> types = new LinkedList<TbQualType>();
			TbQualType qualType = new TbQualType();
			qualType.setQualTypeName(tqt.getQualTypeName());
			qualType.setId(tqt.getId());
			types.add(qualType);
			for (int i = 0;i< types.size();i++) {
				appQualTypePojo.getTypeQualListNames().add(types.get(i).getQualTypeName());
				if (null != tenderDocPojo) {
					if (tenderDocPojo.getQualTypeId().equals(types.get(i).getId()))
							appQualTypePojo.setIndex(i);
				} else 
					appQualTypePojo.setIndex(0);
			}
			appQualTypePojo.setTypeQualList(types);
		}*/
	/*	return appQualTypePojo;
	}*/
	@Override
	public List<NodePojo> findNodesPojo(String processDefinitionId) {
		
		return null;
	}
	/*@Override
	public AppQualTypePojo findQualTypePojo(String originalQualTypeId, String processDefinitionId, String qualTypeId) {
		if (StringUtils.isBlank(qualTypeId))
			qualTypeId = "";
		AppQualTypePojo appQualTypePojo = new AppQualTypePojo();
		appQualTypePojo.setIndex(0);
		// 初始化资质类别
		if (originalQualTypeId.equals("12345678901234567890123456789010")) {//其他
			List<TbQualType> types = iTbQualTypeService.findAllQualTypeUsed();
			
			for (int i = 0;i< types.size();i++) {
			    TbQualType qualType = types.get(i);
				appQualTypePojo.getTypeQualListNames().add(qualType.getQualTypeName());
				appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(qualType.getId()));
				if (qualTypeId.equals(types.get(i).getId()))
						appQualTypePojo.setIndex(i);
			}
			appQualTypePojo.setTypeQualList(types);
		} else if (originalQualTypeId.equals("root")){//河北建研科技有限公司
			List<TbQualType> types=new LinkedList<TbQualType>();
			TbQualType groundWork=new TbQualType();
			groundWork.setId("12345678901234567890123456789002");
			groundWork.setQualTypeName("河北建筑科技有限公司地上施工类");
			types.add(groundWork);
			TbQualType underGroundWork=new TbQualType();
			underGroundWork.setId("12345678901234567890123456789003");
			underGroundWork.setQualTypeName("河北建筑科技有限公司地下施工类");
			types.add(underGroundWork);
			for (int i = 0;i< types.size();i++) {
				appQualTypePojo.getTypeQualListNames().add(types.get(i).getQualTypeName());
				appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(originalQualTypeId));
				if (qualTypeId.equals(types.get(i).getId()))
					appQualTypePojo.setIndex(i);
			}
			appQualTypePojo.setTypeQualList(types);
		} else {
			TbQualType tqt = iTbQualTypeService.findByProcessDefinitionId(processDefinitionId);
			List<TbQualType> types = new LinkedList<TbQualType>();
			TbQualType qualType = new TbQualType();
			qualType.setQualTypeName(tqt.getQualTypeName());
			qualType.setId(tqt.getId());
			types.add(qualType);
			for (int i = 0;i< types.size();i++) {
				appQualTypePojo.getTypeQualListNames().add(types.get(i).getQualTypeName());
				appQualTypePojo.setDictionaryListIndex(iTbDictionaryService.findAllXMLBList(originalQualTypeId));
				if (qualTypeId.equals(types.get(i).getId()))
					appQualTypePojo.setIndex(i);
			}
			appQualTypePojo.setTypeQualList(types);
		}
		return appQualTypePojo;
	}*/

}
