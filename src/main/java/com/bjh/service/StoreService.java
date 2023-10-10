package com.bjh.service;

import com.bjh.entity.Result;
import com.bjh.entity.Store;
import com.bjh.page.Page;

import java.util.List;

public interface StoreService {
    //查询所有仓库

    public List<Store> findAllStore();

    //分页查询仓库的业务方法
    public Page queryStorePage(Page page, Store store);

    //添加仓库的业务方法
    public Result saveStore(Store store);

    //修改仓库的业务方法
    public Result updateStore(Store store);


    //删除仓库的业务方法
    public Result deleteStore(Integer storeId);

    //校验分类编码是否已存在的业务方法
    public Result queryTypeByNum(String storeNum);




}
