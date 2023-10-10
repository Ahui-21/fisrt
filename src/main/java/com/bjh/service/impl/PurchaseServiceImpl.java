package com.bjh.service.impl;

import com.bjh.entity.Result;
import com.bjh.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.bjh.entity.Purchase;
import com.bjh.mapper.PurchaseMapper;
import com.bjh.service.PurchaseService;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public Result insertPurchase(Purchase purchase) {


       /* //补充字段
        purchase.setFactBuyNum(purchase.getBuyNum());*/
        //添加采购单
        int i = purchaseMapper.insertPurchase(purchase);
        if(i>0){
            return Result.ok("采购单添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单添加失败！");
    }

    @Override
    public Page queryPurchasePage(Page page, Purchase purchase) {

        //查询采购单总行数
        Integer purchaseCount = purchaseMapper.selectPurchaseCount(purchase);

        //分页查询采购单
        List<Purchase> purchaseList = purchaseMapper.selectPurchasePage(page,
                purchase);

        //将查询到的总行数和当前页数据组装到Page对象
        page.setTotalNum(purchaseCount);
        page.setResultList(purchaseList);

        return page;
    }

    //删除采购单的业务方法
    @Override
    public Result deletePurchase(Integer buyId) {
        //根据id删除采购单
        int i = purchaseMapper.deletePurchaseById(buyId);
        if(i>0){
            return Result.ok("采购单删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单删除失败！");
    }

    //修改采购单的业务方法
    @Override
    public Result updatePurchase(Purchase purchase) {
        //根据id修改采购单
        int i = purchaseMapper.updatePurchaseById(purchase);
        if(i>0){
            return Result.ok("采购单修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单修改失败！");
    }
}


