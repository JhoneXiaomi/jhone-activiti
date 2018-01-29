package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbCoverPictureDtoMapper;
import com.hebabr.base.service.ICoverPictureService;
import com.hebabr.model.TbCoverPictureDto;
import com.hebabr.model.TbCoverPictureDtoExample;
import com.hebabr.model.TbCoverPictureDtoExample.Criteria;
@Service
public class CoverPictureServiceimpl implements ICoverPictureService{
	@Autowired
	private TbCoverPictureDtoMapper coverMapper;
	@Override
	public void insertCover(TbCoverPictureDto cover) {
		coverMapper.insert(cover);
	}
	@Override
	public TbCoverPictureDto getCoversByArticleId(String articleId) {
		TbCoverPictureDtoExample tode = new TbCoverPictureDtoExample();
		Criteria cc = tode.createCriteria(); 
		cc.andArticleIdEqualTo(articleId).andIsUsedEqualTo(true);
		List<TbCoverPictureDto> list=coverMapper.selectByExample(tode);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public void dropCoverByid(String id) {
		coverMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int updateCoverById(TbCoverPictureDto cover) {
		return coverMapper.updateByPrimaryKey(cover);
	}

}
