package com.bjh.controller;

import com.bjh.dto.AssignRoleDto;
import com.bjh.entity.*;
import com.bjh.mapper.UserMapper;
import com.bjh.page.Page;
import com.bjh.service.RoleService;
import com.bjh.service.UserService;
import com.bjh.utils.TokenUtils;
import com.bjh.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    //分页查询用户
    @RequestMapping("/user-list")
    public Result userList(Page page, User user) {

        //执行业务
        page = userService.queryUserByPage(page, user);

        //响应
        return Result.ok(page);


    }

    //添加用户的接口

    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User user, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {

        //拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);

        int currentUserId = currentUser.getUserId();
        user.setCreateBy(currentUserId);

        //执行业务
        Result result = userService.saveUser(user);

        return result;
    }

    /*
     * 更改用户状态
     *
     * */
    @RequestMapping("/updateState")
    public Result modifyUserState(@RequestBody User user) {

        Result result = userService.updateUserState(user);

        return result;

    }

    /*查询已分配的角色
     *
     * */
    @Autowired
    private RoleService roleService;

    @RequestMapping("/user-role-list/{userId}")
    public Result queryUserRoleByUid(@PathVariable Integer userId) {
        //执行业务
        List<Role> roleList = roleService.selectUserRoleByUid(userId);
        return Result.ok(roleList);

    }

    /*
     * 分配角色
     * */
    @RequestMapping("/assignRole")
    public Result assignRole(@RequestBody AssignRoleDto roleDto) {


        userService.assignRole(roleDto);
        return Result.ok("分配角色成功！");
    }

    /*
     * 删除单个用户
     * */
    @RequestMapping("/deleteUser/{userId}")
    public Result deleteUserById(@PathVariable Integer userId) {
        //执行业务
        Result result = userService.deleteUserByIds(Arrays.asList(userId));
        //响应
        return result;
    }


    /*
     * 批量删除多个用户
     * */

    @RequestMapping("/deleteUserList")
    public Result deleteUserByIds(@RequestBody List<Integer> userIdList) {
        //执行业务
        Result result = userService.deleteUserByIds(userIdList);
        //响应
        return result;
    }

    /*
    * 修改用户
    * */
    @RequestMapping("/updateUser")
    public Result modifyUserNameByUid(@RequestBody User user,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int id = currentUser.getUserId();
        user.setUpdateBy(id);
        //执行业务
        Result result = userService.updateUserNameByUid(user);
        return result;

    }

    /*
    * 重置密码
    * */
    @RequestMapping("/updatePwd/{userId}")
    public Result resetPwdByUid(@PathVariable Integer userId){
        //执行业务
        Result result = userService.updateUserPwdByUid(userId);
        //响应
        return result;
    }

    /*
    * 导出
    * */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page,User user){
        //分页查询仓库
        page = userService.queryUserByPage(page,user);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}