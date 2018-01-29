package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbArticleVsTagDtoMapper;
import com.hebabr.base.dao.TbInFormTagDtoMapper;
import com.hebabr.base.service.IInfoTagService;
import com.hebabr.model.TbInFormTagDto;
import com.hebabr.model.TbArticleVsTagDto;
import com.hebabr.model.TbInFormTagDtoExample;
@Service
public class InfoTagServiceimpl implements IInfoTagService{
	@Autowired
	private TbArticleVsTagDtoMapper articleVsTagMapper;
	@Autowired
	private TbInFormTagDtoMapper tagMapper;

	@Override
	public int newTag(TbInFormTagDto informTag) {
		return tagMapper.insert(informTag);
	};
	
	
	@Override
	public void insertArticleVsTag(TbArticleVsTagDto aVsT) {
		articleVsTagMapper.insert(aVsT);
	}
	@Override
	public void dropByArticleId(String articleId) {
		articleVsTagMapper.dropByArticleId(articleId);
			
	}


	@Override
	public List<TbInFormTagDto> findTagByArticleTypeAndTagName(Integer articleType,String tagName) {
		
		TbInFormTagDtoExample tode = new TbInFormTagDtoExample();
		TbInFormTagDtoExample.Criteria cc = tode.createCriteria();
		cc.andArticleTypeEqualTo(articleType).andTagNameEqualTo(tagName);
				
		return tagMapper.selectByExample(tode);
	}


	@Override
	public List<TbInFormTagDto> findTagByArticleType(Integer articleType) {
		
		TbInFormTagDtoExample tode = new TbInFormTagDtoExample();
		TbInFormTagDtoExample.Criteria cc = tode.createCriteria();
		cc.andArticleTypeEqualTo(articleType);
				
		return tagMapper.selectByExample(tode);
	}
}
