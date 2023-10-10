package com.bjh.service.impl;

import com.alibaba.fastjson.JSON;
import com.bjh.dto.AssignAuthDto;
import com.bjh.entity.Auth;
import com.bjh.mapper.AuthMapper;
import com.bjh.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private AuthMapper authMapper;



    /*
    *
    * 查询用户菜单树的方法
    *
    * */
    @Override
    public List<Auth> queryAuthTreeByUid(Integer userId) {

        //先从redis中查询缓存用户菜单树
        String authTreeJson = redisTemplate.opsForValue().get("authTree:" + userId);
        if (StringUtils.hasText(authTreeJson)){//说明redis有
            //将菜单树List<auth>转的json串转回菜单树并返回
            List<Auth> authTreeList = JSON.parseArray(authTreeJson, Auth.class);

            return authTreeList;

        }
        //说明redis没有
        //查询用户权限的所有菜单

        List<Auth> allAuthList = authMapper.selectAllAuthByUid(userId);

        //将所有菜单转成菜单树

        List<Auth> authTreeList = allAuthToAuthTree(allAuthList,0);
        //向redis中缓存菜单树
        redisTemplate.opsForValue().set("authTree:"+userId,JSON.toJSONString(authTreeList));

        return authTreeList;
    }

    /*
    * //查询整个权限(菜单)树的业务方法
    * */
    @Override
    public List<Auth> allAuthTree() {

        //先从redis中查询缓存
        String allAuthTreeJson = redisTemplate.opsForValue().get("all:authTree");
        if (StringUtils.hasText(allAuthTreeJson)) {//从redis中查到缓存
            //将json串转回整个权限树List<auth>并返回
            List<Auth> authTreeList = JSON.parseArray(allAuthTreeJson, Auth.class);
            return authTreeList;
        }
        //redis中没有查到缓存,从数据库表中查询所有权限(菜单)
        List<Auth> allAuthList = authMapper.getAllAuth();
        //将所有权限(菜单)List<Auth>转成整个权限(菜单)树List<Auth>
        List<Auth> allAuthTreeList = allAuthToAuthTree(allAuthList, 0);
        //将整个权限(菜单)树List<Auth>转成json串并保存到redis
        redisTemplate.opsForValue().set("all:authTree", JSON.toJSONString(allAuthTreeList));
        //返回整个权限(菜单)树List<Auth>
        return allAuthTreeList;

    }


    //给角色分配权限(菜单)的业务方法
    @Transactional//事务处理
    @Override
    public void assignAuth(AssignAuthDto assignAuthDto) {

        //拿到角色id
        Integer roleId = assignAuthDto.getRoleId();
        //拿到给角色分配的所有权限(菜单)id
        List<Integer> authIds = assignAuthDto.getAuthIds();

        //根据角色id删除给角色已分配的所有权限(菜单)
        authMapper.delAuthByRoleId(roleId);

        //循环添加角色权限(菜单)关系
        for (Integer authId : authIds) {
            authMapper.insertRoleAuth(roleId, authId);
        }
    }







    //将所有菜单转成菜单树的递归方法

    private List<Auth> allAuthToAuthTree(List<Auth> allAuthList,Integer pid) {
        //查询一级菜单
        List<Auth> firstLevelAuthList = new ArrayList<>();
        for (Auth auth : allAuthList) {
            if (auth.getParentId()==pid) {
                firstLevelAuthList.add(auth);
            }
        }
        //拿到每一个一级菜单的所有二级菜单
        for (Auth firstAuth : firstLevelAuthList) {
            List<Auth> secondLevelAuthList = allAuthToAuthTree(allAuthList, firstAuth.getAuthId());
            firstAuth.setChildAuth(secondLevelAuthList);
        }
        return firstLevelAuthList;
    }
}
