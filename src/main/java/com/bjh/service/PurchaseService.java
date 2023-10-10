package com.bjh.service;

import com.bjh.entity.Purchase;
import com.bjh.entity.Result;
import com.bjh.page.Page;

/*
添加采购单的业务接口
*/

public interface PurchaseService{


    //添加采购单
    public Result insertPurchase(Purchase purchase);

    //分页查询采购单的业务方法
    public Page queryPurchasePage(Page page, Purchase purchase);

    //删除采购单的业务方法
    public Result deletePurchase(Integer buyId);

    //修改采购单的业务方法
    public Result updatePurchase(Purchase purchase);




}
