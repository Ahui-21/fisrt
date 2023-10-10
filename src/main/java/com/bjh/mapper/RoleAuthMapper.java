package com.bjh.mapper;

import com.bjh.entity.RoleAuth;

import java.util.List;

public interface RoleAuthMapper {

    /*
    * 根据角色id删除角色权限关系方法
    * */

    public int deleteRoleAuthByRid(Integer roleId);
;
}