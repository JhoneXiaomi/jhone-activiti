package com.hebabr.base.util;

import java.util.ArrayList;
import java.util.List;
import com.hebabr.base.pojo.MenuPojo;
import com.hebabr.model.TbMenuDto;

public final class BuildTbresourceTreeNode {

	/**
	 * 将list集合整理为菜单树结构。
	 * 
	 * @param list
	 *            菜单集合
	 * @param platId
	 *            主菜单ID
	 * @return 菜单树集合
	 * @throws Exception
	 */
	public synchronized static List<MenuPojo> buildMenuTreeByList(final List<TbMenuDto> list, String platId) throws Exception {
		List<MenuPojo> pojoList = new ArrayList<MenuPojo>();
		return pojoList;
	}
	
 }
