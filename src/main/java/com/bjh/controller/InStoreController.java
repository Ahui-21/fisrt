package com.bjh.controller;

import com.bjh.entity.InStore;
import com.bjh.entity.Result;
import com.bjh.entity.Store;
import com.bjh.entity.User;
import com.bjh.page.Page;
import com.bjh.service.InStoreService;
import com.bjh.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/instore")
@RestController
public class InStoreController {

    //注入StoreService
    @Autowired
    private StoreService StoreService;

    @Autowired
    private InStoreService inStoreService;

    /**
     * 查询所有仓库的url接口/instore/store-list
     */
    @RequestMapping("/store-list")
    public Result storeList(){
        //执行业务
        List<Store> storeList = StoreService.findAllStore();
        //响应
        return Result.ok(storeList);
    }

    /**
      分页查询入库单的url接口/instore/instore-page-list

     */

    @RequestMapping("/instore-page-list")
    public Result inStorePageList(Page page, InStore inStore){
        //执行业务
        page = inStoreService.queryInStorePage(page, inStore);
        //响应
        return Result.ok(page);
    }


    /**
     * 确定入库的url接口/instore/instore-confirm
     *
     * @RequestBody InStore inStore将请求传递的json数据封装到参数InStore对象;
     */
    @RequestMapping("/instore-confirm")
    public Result confirmInStore(@RequestBody InStore inStore){
        //执行业务
        Result result = inStoreService.confirmInStore(inStore);
        //响应
        return result;
    }

    /*
     * 导出
     * */
    @RequestMapping("/exportTable")
    public Result exportTable(Page page, InStore inStore){
        //分页查询仓库
        page = inStoreService.queryInStorePage(page,inStore);
        //拿到当前页数据
        List<?> resultList = page.getResultList();
        //响应
        return Result.ok(resultList);

    }
}
