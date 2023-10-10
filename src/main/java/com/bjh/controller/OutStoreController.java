package com.bjh.controller;

import com.bjh.entity.*;
import com.bjh.page.Page;
import com.bjh.service.OutStoreService;
import com.bjh.service.StoreService;
import com.bjh.utils.TokenUtils;
import com.bjh.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outstore")
public class OutStoreController {

    @Autowired
    private OutStoreService outStoreService;
    @Autowired
    private StoreService storeService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

        /*
        * 添加出库单
        * */
    @RequestMapping("/outstore-add")
    public Result addOutStore(@RequestBody OutStore outStore,
                              @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){

        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id,即添加出库单的用户id
        int createBy = currentUser.getUserId();
        outStore.setCreateBy(createBy);

        //执行业务
        Result result = outStoreService.saveOutStore(outStore);

        //响应
        return result;
    }
    /**
     * 查询所有仓库的url接口/outstore/store-list
     */
    @RequestMapping("/store-list")
    public Result storeList(){
        //执行业务
        List<Store> storeList = storeService.findAllStore();
        return Result.ok(storeList);
    }
    /**
     * 分页查询出库单的url接口/outstore/outstore-page-list

     */
    @RequestMapping("/outstore-page-list")
    public Result outStorePageList(Page page, OutStore outStore){
        //执行业务
        page = outStoreService.outStorePage(page, outStore);
        //响应
        return Result.ok(page);
    }
    /**
     * 确定出库的url接口/outstore/outstore-confirm

     */
    @RequestMapping("/outstore-confirm")
    public Result confirmOutStore(@RequestBody OutStore outStore){
        //执行业务
        Result result = outStoreService.confirmOutStore(outStore);
        //响应
        return result;
    }

    /*
     * 导出
     * */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page,OutStore outStore){
        //分页查询仓库
        page = outStoreService.outStorePage(page,outStore);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);
    }
}
