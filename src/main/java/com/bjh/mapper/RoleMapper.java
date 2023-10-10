package com.bjh.mapper;

import com.bjh.entity.Role;
import com.bjh.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {

    //查询所有角色
    public List<Role> selectAllRole();
    //根据id查询用户分配的所有角色
    public List<Role> selectUserRoleByUid(Integer userId);
    //根据角色名查询角色id

    public Integer selectRoleIdByName(String roleName);

    //查询角色总行数
    public Integer selectRoleRowCount(Role role);
    //分页查询角色

    public List<Role> selectRolePage(@Param("page") Page page, @Param("role") Role role);

    //根据角色名称或者角色代码查询角色的方法
    public Role selectRoleByNameOrCode(String roleName, String roleCode);

    //添加角色的方法
    public int insertRole(Role role);

    //根据角色id修改角色状态的方法
    public int updateRoleState(Integer roleId,String roleState);

    //根据角色id删除角色的方法
    public int deleteRoleById(Integer roleId);

    //查询角色已分配的权限菜单
    public List<Integer> selectAuthByIds(Integer roleId);

    //根据角色id修改角色描述的方法
    public int updateDescById(Role role);



}
