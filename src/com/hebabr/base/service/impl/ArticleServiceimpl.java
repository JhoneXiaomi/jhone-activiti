package com.hebabr.base.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbArticleDtoMapper;
import com.hebabr.base.dao.TbArticleVsTagDtoMapper;
import com.hebabr.base.pojo.ArticleAndTagPojo;
import com.hebabr.base.pojo.ArticlePojo;
import com.hebabr.base.pojo.CoverPicturePojo;
import com.hebabr.base.service.IArticleService;
import com.hebabr.model.TbArticleDto;
import com.hebabr.model.TbArticleVsTagDto;
import com.hebabr.model.TbArticleVsTagDtoExample;
import com.hebabr.model.TbInFormTagDto;
@Service
public class ArticleServiceimpl implements IArticleService{
	@Autowired
	private TbArticleDtoMapper articleMapper;
	@Autowired
	private TbArticleVsTagDtoMapper articleVsTagMapper;
	
	@Override
	public void insertArticle(TbArticleDto arcitle) {
		articleMapper.insert(arcitle);
	}

	@Override
	public List<ArticlePojo> getAllUsedArticlePojosByTypeId(Integer typeId) {
		return articleMapper.getAllUsedArticlePojosByTypeId(typeId);
	}

	@Override
	public void dropArticleById(String id) {
		articleMapper.deleteByPrimaryKey(id);
			
	}

	@Override
	public List<ArticlePojo> getAllUsedArticlePojosByTypeIdAndIsTop(Integer typeId, Boolean isTop) {
		return articleMapper.getAllusedArticlePojosByTypeIdAndIsTop(typeId, isTop);
	}

	@Override
	public TbArticleDto getArticleById(String id) {
		
		return articleMapper.selectByPrimaryKey(id);
	}

	@Override
	public ArticleAndTagPojo getArticleAndTagPojoByActicleId(String articleId) throws UnsupportedEncodingException {
		
	
		TbArticleDto article = articleMapper.selectByPrimaryKey(articleId);
		ArticleAndTagPojo articleAndPojo = new ArticleAndTagPojo();
		articleAndPojo.setArticleId(article.getId());
		articleAndPojo.setTitle(article.getTitle());
		articleAndPojo.setIsTop(article.getIsTop());
		articleAndPojo.setContent(new String(article.getContent(),"UTF-8"));
		
		TbArticleVsTagDtoExample tode = new TbArticleVsTagDtoExample();
		TbArticleVsTagDtoExample.Criteria cc = tode.createCriteria();
		cc.andArticleIdEqualTo(articleId);
		List<TbArticleVsTagDto> tags = articleVsTagMapper.selectByExample(tode);
		List<String> tagIds = new ArrayList<String>();
		for(int i=0;i<tags.size();i++){
			tagIds.add(tags.get(i).getTagId());
		}
		articleAndPojo.setTagId(tagIds);
		return articleAndPojo;
	}

	@Override
	public List<ArticlePojo> getArticlePojoListOrderByTypeId(Integer articleType) {
		
		return articleMapper.getArticlePojoListOrderByTypeId(articleType);
	}

	@Override
	public List<CoverPicturePojo> getTopFiveCoversByArticle() {
		return articleMapper.getTopFiveCoversByArticle();
	}

	@Override
	public void updateArticle(TbArticleDto article) {
		articleMapper.updateArticleById(article);
		
	}

	@Override
	public List<TbInFormTagDto> getTagByArticleId(String articleId) {
		
		return articleMapper.getTagDtoByArticleId(articleId);
	}

	@Override
	public int cancleTopByArticleId(TbArticleDto article) {
		
		return articleMapper.cancleTopByArticleId(article);
	}



	
}
