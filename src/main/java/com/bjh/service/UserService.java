package com.bjh.service;


import com.bjh.dto.AssignRoleDto;
import com.bjh.entity.Result;
import com.bjh.entity.User;
import com.bjh.page.Page;

import java.util.List;

/*
* user_info表的service接口
* */
public interface UserService {

    /*
    * 根据账号查询用户
    * */
    public User queryUserByCode(String userCode);

    //分页查询用户的业务方法
    public Page queryUserByPage(Page page,User user);

    //添加用户方法
    public Result saveUser(User user);

    //修改用户状态
    public Result  updateUserState(User user);

    //给用户分配角色
    public void assignRole(AssignRoleDto assignRoleDto);

    //删除用户
    public  Result deleteUserByIds(List<Integer> userIdList);

    //修改用户
    public Result updateUserNameByUid(User user);

    //修改用户密码
    public Result updateUserPwdByUid(Integer userId);
}
