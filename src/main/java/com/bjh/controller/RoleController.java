package com.bjh.controller;

import com.bjh.dto.AssignAuthDto;
import com.bjh.dto.AssignRoleDto;
import com.bjh.entity.CurrentUser;
import com.bjh.entity.Result;
import com.bjh.entity.Role;
import com.bjh.entity.User;
import com.bjh.page.Page;
import com.bjh.service.AuthService;
import com.bjh.service.RoleService;
import com.bjh.utils.TokenUtils;
import com.bjh.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthService authService;


    /*
     * 查询所有角色
     * */
    @RequestMapping("/role-list")
    public Result queryAllRole(){

        List<Role> roleList = roleService.selectAllRole();
        return Result.ok(roleList);

    }


    /*
    * 分页查询角色列表
    * */
    @RequestMapping("/role-page-list")
    public Result roleListPage(Page page,Role role){
        //执行业务
         page = roleService.queryRolePage(page, role);
         //响应
         return Result.ok(page);

    }

    /*
    * 添加角色
    *
    * */
    //{roleName: "统计", roleDesc: "统计", roleCode: "tonmgji"}
    //roleCode:"tonmgji"
    //roleDesc:"统计"
    //roleName:"统计"
    @RequestMapping("/role-add")
    public Result addRole(@RequestBody Role role, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int roleId = currentUser.getUserId();
        role.setCreateBy(roleId);
        //执行业务
        Result result = roleService.addRole(role);
        //响应
        return result;

    }

    /*
    * 修改状态
    * role-state-update
    * */
    @RequestMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role){

        Result result = roleService.setRoleState(role);
        return result;

    }
    /*
    * 删除角色
    *
    * */
    @RequestMapping("/role-delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId){
        //执行业务
        Result result = roleService.deleteRoleByRId(roleId);
        //响应
        return result;
    }

    /*
    *查询角色已分配的权限(菜单)
    * role/role-auth?roleId=1&_t=1695021274397
    * */


    @RequestMapping("/role-auth")
    public Result queryRoleAuth(Integer roleId){
        //执行业务
        List<Integer> authIdList = roleService.queryAuthIds(roleId);
        //响应
        return Result.ok(authIdList);
    }


    /**
     * 给角色分配权限(菜单)的url接口/role/auth-grant
     *
     * @RequestBody AssignAuthDto assignAuthDto将请求传递的json数据
     * 封装到参数AssignAuthDto对象中;
     */
    @RequestMapping("/auth-grant")
    public Result assignAuth(@RequestBody AssignAuthDto assignAuthDto){
        //执行业务
        authService.assignAuth(assignAuthDto);
        //响应
        return Result.ok("分配权限成功！");
    }

    /*
    * 修改角色信息
    * role/role-update
    * */
    @RequestMapping("/role-update")
    public Result updateRole(@RequestBody Role role,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id -- 修改角色的用户id
        int updateBy = currentUser.getUserId();

        role.setUpdateBy(updateBy);

        //执行业务
        Result result = roleService.updateRoleDesc(role);

        //响应
        return result;
    }

    /*
     * 导出
     * */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page,Role role){
        //分页查询仓库
        page = roleService.queryRolePage(page,role);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}
