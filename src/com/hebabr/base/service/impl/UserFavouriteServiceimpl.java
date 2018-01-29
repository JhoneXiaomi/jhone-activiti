package com.hebabr.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbUserFavouriteDtoMapper;
import com.hebabr.base.pojo.UserOrgPojo;
import com.hebabr.base.service.IUserFavouriteService;
import com.hebabr.model.TbUserFavouriteDto;
import com.hebabr.model.TbUserFavouriteDtoExample;
@Service
public class UserFavouriteServiceimpl implements IUserFavouriteService{
	@Autowired
	private TbUserFavouriteDtoMapper userFavouriteMapper;
	
	@Override
	public void insertFavourite(TbUserFavouriteDto uf) {
		userFavouriteMapper.insert(uf);
	}

	@Override
	public List<UserOrgPojo> getFavouriteUserOrgPojoByUserId(String userId) {
		
		return userFavouriteMapper.getFavouriteUserOrgPojoByUserId(userId);
	}

	@Override
	public TbUserFavouriteDto getFavouriteByUserIdAndFavouriteId(String userId, String FavouriteId) {
		TbUserFavouriteDtoExample tgde = new TbUserFavouriteDtoExample();
		tgde.createCriteria().andFavouriteIdEqualTo(FavouriteId).andUserIdEqualTo(userId);
		List<TbUserFavouriteDto> list= userFavouriteMapper.selectByExample(tgde);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void deleteFavouriteByUserIdAndFavouriteId(String userId, String favouriteId) {
		userFavouriteMapper.deleteByUserIdAndFavouriteId(userId, favouriteId);
		
	}
	
}
