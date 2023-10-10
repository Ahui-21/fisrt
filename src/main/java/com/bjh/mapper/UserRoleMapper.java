package com.bjh.mapper;

import com.bjh.entity.UserRole;

public interface UserRoleMapper {

    //根据用户id删除用户已经分配的角色关系
    public int deleteUserRoleByUid(Integer userId);

    //添加用户角色关系
    public int insertUserRole(UserRole userRole);

}