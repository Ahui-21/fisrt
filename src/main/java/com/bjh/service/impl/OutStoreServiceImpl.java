package com.bjh.service.impl;

import com.bjh.entity.Product;
import com.bjh.entity.Result;
import com.bjh.mapper.ProductMapper;
import com.bjh.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.bjh.mapper.OutStoreMapper;
import com.bjh.entity.OutStore;
import com.bjh.service.OutStoreService;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAForUserEnc;

import java.util.List;

@Service
public class OutStoreServiceImpl implements OutStoreService{


    @Autowired
    private OutStoreMapper outStoreMapper;

    @Autowired
    private ProductMapper productMapper;
    //添加出库单
    @Override
    public Result saveOutStore(OutStore outStore) {

        int i = outStoreMapper.insertOutStore(outStore);
        if(i>0){
            return Result.ok("添加出库单成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "添加出库单失败！");
    }

    @Override
    public Page outStorePage(Page page, OutStore outStore) {

        //查询出库单行数
        int count = outStoreMapper.outStoreCount(outStore);
        //分页查询
        List<OutStore> outStoreList = outStoreMapper.outStorePage(page, outStore);
        //将查询的结果封装到page对象中
        page.setTotalNum(count);
        page.setResultList(outStoreList);
        return page;
    }

    @Transactional
    @Override
    public Result confirmOutStore(OutStore outStore) {
        //根据商品id查询商品
        Product product = productMapper.selectProductById(outStore.getProductId());
        if(outStore.getOutNum()>product.getProductInvent()){
            return Result.err(Result.CODE_ERR_BUSINESS, "商品库存不足");
        }

        //根据id将出库单状态改为已出库
        int i = outStoreMapper.updateIsOutById(outStore.getOutsId());
        if(i>0){
            //根据商品id减商品库存
            int j = productMapper.addInventById(outStore.getProductId(),
                    -outStore.getOutNum());
            if(j>0){
                return Result.ok("出库成功！");
            }
            return Result.err(Result.CODE_ERR_BUSINESS, "出库失败！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "出库失败！");
    }
}

