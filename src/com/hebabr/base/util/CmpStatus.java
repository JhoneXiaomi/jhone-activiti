package com.hebabr.base.util;

import java.io.Serializable;

/**
 * @author DuanYinghua
 * @date 2014-11-28
 * 统一的后台返回类，返到前台后会自动转成json格式的串
 *  
 */

public class CmpStatus implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String status;       //  1代表成功，0代表失败，其他自定义
	private String comment;   //  自定义的说明
	
	public CmpStatus(String status, String comment){
		setStatus(status);
		setComment(comment);
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
