package com.bjh.service.impl;

import com.bjh.entity.Result;
import com.bjh.entity.Store;
import com.bjh.mapper.StoreMapper;
import com.bjh.page.Page;
import com.bjh.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "com.bjh.service.impl.StoreServiceImpl")
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    //查询所有仓库的方法
    @CacheEvict(key = "'all:store'")
    @Override
    public List<Store> findAllStore() {
        return storeMapper.selectAllStore();
    }


    //分页查询仓库的业务方法
    @Override
    public Page queryStorePage(Page page, Store store) {
        //查询仓库总行数
        int storeCount = storeMapper.selectStoreCount(store);

        //分页查询仓库
        List<Store> storeList = storeMapper.selectStorePage(page, store);

        //将查到的总行数和当前页数据封装到Page对象
        page.setTotalNum(storeCount);
        page.setResultList(storeList);

        return page;
    }

    //添加仓库
    @Override
    public Result saveStore(Store store) {

        int i = storeMapper.insertStore(store);
        if(i>0){
            return Result.ok("仓库添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库添加失败！");
    }

    //修改仓库的业务方法
    @Override
    public Result updateStore(Store store) {
        //根据仓库id修改仓库
        int i = storeMapper.updateStoreById(store);
        if(i>0){
            return Result.ok("仓库修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库修改失败！");
    }

    //删除仓库的业务方法
    @Override
    public Result deleteStore(Integer storeId) {
        //根据仓库id删除仓库
        int i = storeMapper.deleteStoreById(storeId);
        if(i>0){
            return Result.ok("仓库删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "仓库删除失败！");
    }

    //校验分类编码是否已存在的业务方法
    @Override
    public Result queryTypeByNum(String storeNum) {

        Store typeBystoreNum = storeMapper.findTypeBystoreNum(storeNum);

        return Result.ok(typeBystoreNum==null);
    }
}
