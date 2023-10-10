package com.bjh.service;

import com.bjh.dto.AssignAuthDto;
import com.bjh.entity.Auth;
import java.util.List;

public interface AuthService {

	//根据用户id查询用户权限(菜单)树的业务方法
	public List<Auth> queryAuthTreeByUid(Integer userId);
	//查询整个权限菜单树的方法
	public List<Auth> allAuthTree();

	//给角色分配权限(菜单)的业务方法
	public void assignAuth(AssignAuthDto assignAuthDto);


}
