package com.bjh.service.impl;

import com.bjh.dto.AssignRoleDto;
import com.bjh.entity.Result;
import com.bjh.entity.User;
import com.bjh.entity.UserRole;
import com.bjh.mapper.RoleMapper;
import com.bjh.mapper.UserMapper;
import com.bjh.mapper.UserRoleMapper;
import com.bjh.page.Page;
import com.bjh.service.UserService;
import com.bjh.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.perc.PercInstantiator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    /*
     * 根据账号查询用户
     * */

    @Override
    public User queryUserByCode(String userCode) {
        return userMapper.selectUserByCode(userCode);
    }


    /*
    * 分页查询
    * */

    @Override
    public Page queryUserByPage(Page page, User user) {

        //查询用户行书
        Integer count = userMapper.selectUserRowCount(user);
        //分页查询用户
        List<User> userList = userMapper.selectUserByPage(page, user);
        //组装分页信息
        page.setTotalNum(count);
        page.setResultList(userList);
        return page;
    }

    /*
    * 添加用户
    * */

    @Override
    public Result saveUser(User user) {

        //判断用户是否存在
        User u = userMapper.selectUserByCode(user.getUserCode());
        if (u!=null){
            //账号已存在
            return Result.err(Result.CODE_ERR_BUSINESS,"账号已存在！");
        }


        //对密码进行加密
        String password = DigestUtil.hmacSign(user.getUserPwd());
        user.setUserPwd(password);
        //执行添加
        int i = userMapper.insertUser(user);
        if (i > 0){
            return Result.ok("添加成功！");
        }else {

            return Result.err(Result.CODE_ERR_BUSINESS,"添加失败！");
        }
    }

    /*
    * 更改用户状态
    * */
    @Override
    public Result updateUserState(User user) {

        int ret = userMapper.updateUserState(user.getUserState(),user.getUserId());
        if (ret > 0){
            return Result.ok("修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"修改失败！");
    }


    /*
    * 给用户分配角色
    *
    * */
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Transactional
    @Override
    public void assignRole(AssignRoleDto assignRoleDto) {

        userRoleMapper.deleteUserRoleByUid(assignRoleDto.getUserId());

        List<String> roleNameList = assignRoleDto.getRoleCheckList();

        for (String roleName:roleNameList) {
            Integer roleId = roleMapper.selectRoleIdByName(roleName);
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(assignRoleDto.getUserId());
            userRoleMapper.insertUserRole(userRole);

        }

    }


    /*
    * 删除用户
    * */
    @Override
    public Result deleteUserByIds(List<Integer> userIdList) {

        int ret = userMapper.setIsDeleteUserByUid(userIdList);
        if (ret > 0){
            return Result.ok("删除成功！");

        }else {
            return Result.err(Result.CODE_ERR_BUSINESS,"删除失败！");
        }
    }

    @Override
    public Result updateUserNameByUid(User user) {

        int ret = userMapper.updateUserNameByUid(user);
        if (ret > 0){
            return Result.ok("修改成功!");
        }else {
            return Result.err(Result.CODE_ERR_BUSINESS,"修改失败！");
        }

    }


    /*
    * 重置密码
    * */
    @Override
    public Result updateUserPwdByUid(Integer userId) {

        //给定初始密码并加密

        String pwd = DigestUtil.hmacSign("123456");
        //根据id修改密码
        int ret = userMapper.updateUserPwdByUid(pwd, userId);
        if (ret > 0){
            return Result.ok("重置成功！");
        }else {
            return Result.err(Result.CODE_ERR_BUSINESS,"重置失败！");

        }
    }
}
