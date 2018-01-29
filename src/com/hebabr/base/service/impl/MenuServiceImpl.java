package com.hebabr.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hebabr.base.dao.TbMenuDtoMapper;
import com.hebabr.base.pojo.MenuPojo;
import com.hebabr.base.service.IMenuService;
import com.hebabr.model.TbFunctionDto;
import com.hebabr.model.TbMenuDto;
import com.hebabr.model.TbMenuDtoExample;

@Service
public class MenuServiceImpl implements IMenuService {
	
	@Autowired
    private TbMenuDtoMapper menuMapper; 
	@Autowired
	private IMenuService menuService;

	@Override
	public TbMenuDto queryMenuById(String id) {
		// TODO Auto-generated method stub
		return menuMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<TbMenuDto> getMenusByParentId(String parentId) {
		// TODO Auto-generated method stub
		TbMenuDtoExample tode = new TbMenuDtoExample();
		com.hebabr.model.TbMenuDtoExample.Criteria cc = tode.createCriteria();
		cc.andParentIdEqualTo(parentId);
		return menuMapper.selectByExample(tode);
	}


	@Override
	public List<List<TbMenuDto>> getLinkMenuListsByFunctionList(List<TbMenuDto> functionList) {
		// TODO Auto-generated method stub
		//集合中是LinkList(父，子);
		List<List<TbMenuDto>> linkMenuLists=new ArrayList<List<TbMenuDto>>();
		if(functionList!=null&&functionList.size()>0){
			for(TbMenuDto f:functionList){
				String menuId=f.getId();
				if(menuId!=null){
					List<TbMenuDto> linkMenus=new LinkedList<TbMenuDto>();
					if(menuService.getUsedMenuById(menuId).getParentId().equals("0")){
						//已经是第一级菜单
						TbMenuDto menu0=menuService.getUsedMenuById(menuId);
						if(menu0!=null){
							linkMenus.add(menu0);
						}
						//System.out.println("sortNo"+menuMapper.selectByPrimaryKey(menuId).getSortNo());
					}else if(menuService.getUsedMenuById(menuService.getUsedMenuById(menuId).getParentId()).getParentId().equals("0")){
						//父节点的pId==0 是第二级菜单
						TbMenuDto menu2=menuService.getUsedMenuById(menuId);
						TbMenuDto menu1=menuService.getUsedMenuById(menu2.getParentId());
						if(menu1!=null){
							linkMenus.add(menu1);
						}
						if(menu2!=null){
							linkMenus.add(menu2);
						}
					}else{
						//第三级菜单
						TbMenuDto menu3=menuService.getUsedMenuById(menuId);
						TbMenuDto menu2=menuService.getUsedMenuById(menu3.getParentId());
						TbMenuDto menu1=menuService.getUsedMenuById(menu2.getParentId());
						if(menu1!=null){
							linkMenus.add(menu1);
						}
						if(menu2!=null){
							linkMenus.add(menu2);
						}
						if(menu3!=null){
							linkMenus.add(menu3);
						}
					}
					if(linkMenus!=null&&linkMenus.size()>0){
						linkMenuLists.add(linkMenus);
					}
				}
				
			}
		}
		return linkMenuLists;
	}

	@Override
	public TbMenuDto getUsedMenuById(String id) {
		if(id!=null){
			TbMenuDtoExample tode = new TbMenuDtoExample();
			com.hebabr.model.TbMenuDtoExample.Criteria cc = tode.createCriteria();
			cc.andIsUsedEqualTo(true).andIdEqualTo(id);
			List<TbMenuDto> menuList=menuMapper.selectByExample(tode);
			if(menuList!=null&&menuList.size()>0){
				return menuList.get(0);
			}
		}
		return null;

	}

	@Override
	public List<TbMenuDto> getAllMenuUsed() {
		TbMenuDtoExample tode = new TbMenuDtoExample();
		com.hebabr.model.TbMenuDtoExample.Criteria cc = tode.createCriteria();
		cc.andIsUsedEqualTo(true);
		return menuMapper.selectByExample(tode);
	}

	@Override
	public TbMenuDto getMenuByFunctionId(String functionId) {
		TbMenuDtoExample tode = new TbMenuDtoExample();
		com.hebabr.model.TbMenuDtoExample.Criteria cc = tode.createCriteria();
		cc.andIsUsedEqualTo(true).andFunctionIdEqualTo(functionId);
		List<TbMenuDto> menuList=menuMapper.selectByExample(tode);
		if(menuList!=null&&menuList.size()>0){
			return menuList.get(0);
		}
		return null;
	}

	@Override
	public void updateMenu(TbMenuDto menu) {
		menuMapper.updateByPrimaryKey(menu);
		
	}

	@Override
	public TbMenuDto getMenuByName(String menuName) {
		TbMenuDtoExample tode = new TbMenuDtoExample();
		com.hebabr.model.TbMenuDtoExample.Criteria cc = tode.createCriteria();
		cc.andMenuNameEqualTo(menuName);
		List<TbMenuDto> menuList= menuMapper.selectByExample(tode);
		if(menuList!=null&&menuList.size()>0){
			return menuList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public int getMaxNumOfMenu(String parentId) {
		return menuMapper.countByParentId(parentId);
	}

	@Override
	public void insertMenu(TbMenuDto menu) {
		menuMapper.insert(menu);
			
	}

	@Override
	public void dropMenuById(String menuId) {
		menuMapper.deleteByPrimaryKey(menuId);
	}

	@Override
	public MenuPojo getMenuPojo(List<TbMenuDto> menuList) {
		  if(menuList!=null&&menuList.size()>0){
	            List<List<TbMenuDto>> listMenuList=menuService.getLinkMenuListsByFunctionList(menuList);
	            if(listMenuList!=null&&listMenuList.size()>0){
	                List<TbMenuDto> oneLevelMenuList=new ArrayList<TbMenuDto>();
	                List<TbMenuDto> twoLevelMenuList=new ArrayList<TbMenuDto>();
	                List<TbMenuDto> threeLevelMenuList=new ArrayList<TbMenuDto>();
	                Set<TbMenuDto> oneSet=new HashSet<TbMenuDto>();
	                Set<TbMenuDto> twoSet=new HashSet<TbMenuDto>();
	                Set<TbMenuDto> threeSet=new HashSet<TbMenuDto>();
	                //遍历listMenuList将第一项，第 二项，第三项放入不同的set中
	                for(List<TbMenuDto> menuList1:listMenuList){
	                    if(menuList1.size()==1){
	                        oneSet.add(menuList1.get(0));
	                    }else if(menuList1.size()==2){
	                        oneSet.add(menuList1.get(0));
	                        twoSet.add(menuList1.get(1));
	                    }else if(menuList1.size()==3){
	                        oneSet.add(menuList1.get(0));
	                        twoSet.add(menuList1.get(1));
	                        threeSet.add(menuList1.get(2));
	                    }else{
	                        continue;
	                    }
	                }
	                //得到不同层次的menuList
	                oneLevelMenuList.addAll(oneSet);
	                twoLevelMenuList.addAll(twoSet);
	                threeLevelMenuList.addAll(threeSet);
	                //根据sortNo排序
	                Collections.sort(oneLevelMenuList);
	                Collections.sort(twoLevelMenuList);
	                Collections.sort(threeLevelMenuList);
	                
	                //遍历添加到menuPojo指定位置
	                MenuPojo pojo=new MenuPojo();
	                pojo.setId("root");
	                pojo.setMenuName("root");
	                List<MenuPojo> pojoList1=new ArrayList<MenuPojo>();
	                if(oneLevelMenuList!=null&&oneLevelMenuList.size()>0){
	                    for(TbMenuDto menu1:oneLevelMenuList){
	                        //第一级菜单
	                        MenuPojo pojo1=new MenuPojo();
	                        pojo1.setId(menu1.getId());
	                        pojo1.setMenuName(menu1.getMenuName());
	                        pojo1.setUrl(menu1.getMenuUrl());
	                        pojo1.setIcon(menu1.getMenuIcon());
	                        List<MenuPojo> pojoList2=new ArrayList<MenuPojo>();
	                        if(twoLevelMenuList!=null&&twoLevelMenuList.size()>0){
	                            for(TbMenuDto menu2:twoLevelMenuList){
	                                //第二级菜单
	                                if(menu2.getParentId().equals(menu1.getId())){
	                                    MenuPojo pojo2=new MenuPojo();
	                                    pojo2.setId(menu2.getId());
	                                    pojo2.setMenuName(menu2.getMenuName());
	                                    pojo2.setUrl(menu2.getMenuUrl());
	                                    List<MenuPojo> pojoList3=new ArrayList<MenuPojo>();
	                                    if(threeLevelMenuList!=null&&threeLevelMenuList.size()>0){
	                                        for(TbMenuDto menu3:threeLevelMenuList){
	                                            //第三级菜单
	                                            if(menu3.getParentId().equals(menu2.getId())){
	                                                MenuPojo pojo3=new MenuPojo();
	                                                pojo3.setId(menu3.getId());
	                                                pojo3.setMenuName(menu3.getMenuName());
	                                                pojo3.setUrl(menu3.getMenuUrl());
	                                                pojoList3.add(pojo3);
	                                            }
	                                        }
	                                    }
	                                    pojo2.setMenuPojoList(pojoList3);
	                                    pojoList2.add(pojo2);
	                                }
	                            }
	                        }
	                        pojo1.setMenuPojoList(pojoList2);
	                        pojoList1.add(pojo1);
	                        
	                    }
	                }
	                pojo.setMenuPojoList(pojoList1);
	                return pojo;
	            }else{
	                return null;
	            }
	        }else{
	            return null;
	        }
	}


}
