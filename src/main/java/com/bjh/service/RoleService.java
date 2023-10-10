package com.bjh.service;


import com.bjh.entity.Result;
import com.bjh.entity.Role;
import com.bjh.page.Page;

import javax.annotation.Resource;
import java.util.List;
public interface RoleService {

    //查询所有角色

    public List<Role> selectAllRole();
    //根据id查询已分配的角色
    public List<Role> selectUserRoleByUid(Integer userId);

    //分页查询角色的业务方法
    public Page queryRolePage(Page page, Role role);

    //添加角色
    public Result addRole(Role role);

    //启用或禁用
    public Result setRoleState(Role role);

    //删除角色的业务方法
    public Result deleteRoleByRId(Integer roleId);

    //查询角色已分配的权限(菜单)的业务方法
    public List<Integer> queryAuthIds(Integer roleId);

    //修改角色描述的业务方法
    public Result updateRoleDesc(Role role);





}
