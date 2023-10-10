package com.bjh.service.impl;

import com.bjh.entity.Result;
import com.bjh.entity.Role;
import com.bjh.mapper.RoleAuthMapper;
import com.bjh.mapper.RoleMapper;
import com.bjh.page.Page;
import com.bjh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.bjh.service.impl.RoleServiceImpl")
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleAuthMapper roleAuthMapper;

    //查询所有角色方法
    @Cacheable(key = "'all:role'")
    @Override
    public List<Role> selectAllRole() {
        return roleMapper.selectAllRole();
    }

    @Override
    public List<Role> selectUserRoleByUid(Integer userId) {
        return roleMapper.selectUserRoleByUid(userId);
    }

    /*
    * 分页查询角色
    * */

    @Override
    public Page queryRolePage(Page page, Role role) {
        //查询角色总行数
        Integer rowCount = roleMapper.selectRoleRowCount(role);
        //分页查询角色
        List<Role> roleList = roleMapper.selectRolePage(page, role);
        //组装分页信息
        page.setTotalNum(rowCount);
        page.setResultList(roleList);
        return page;
    }

    /*
    * 添加用户
    * */
    @CacheEvict(key = "'all:role'")
    @Override
    public Result addRole(Role role) {

        //判断是否存在
        Role user = roleMapper.selectRoleByNameOrCode(role.getRoleName(), role.getRoleCode());
        if (user!=null){// 已存在用户
            return Result.err(Result.CODE_ERR_BUSINESS,"用户已经存在！");
        }else {
            //不存在进行添加用户操作
            int i = roleMapper.insertRole(role);
            if (i>0){
                return Result.ok("添加成功!");

            }
            return Result.err(Result.CODE_ERR_BUSINESS,"添加失败");

        }
    }

    /*
    * //启用或禁用
    * */
    @CacheEvict(key = "'all:role'")
    @Override
    public Result setRoleState(Role role) {

        int i = roleMapper.updateRoleState(role.getRoleId(),role.getRoleState());
        if(i>0){
            return Result.ok("修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改失败！");

    }

    /*
    * 删除角色
    * */
    @CacheEvict(key = "'all:role'")
    @Override
    public Result deleteRoleByRId(Integer roleId) {

        int i = roleMapper.deleteRoleById(roleId);
        if(i>0){
            //删除角色权限关系
            roleAuthMapper.deleteRoleAuthByRid(roleId);
            return Result.ok("角色删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色删除失败！");

    }


    /*
    *  //查询角色已分配的权限(菜单)的业务方法
    * */
    @Override
    public List<Integer> queryAuthIds(Integer roleId) {
        //根据角色id查询角色已分配的所有权限(菜单)的id
        return roleMapper.selectAuthByIds(roleId);
    }



    //修改角色描述的业务方法
    @CacheEvict(key = "'all:role'")
    @Override
    public Result updateRoleDesc(Role role) {

        //根据角色id修改角色描述
        int i = roleMapper.updateDescById(role);
        if(i>0){
            return Result.ok("角色修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "角色修改失败！");
    }
}
