package com.bjh.mapper;

import com.bjh.entity.Store;
import com.bjh.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreMapper {

    //查询所有仓库
    public List<Store>selectAllStore();


    //查询仓库总行数的方法
    public int selectStoreCount(Store store);

    //分页查询仓库的方法
    public List<Store> selectStorePage(@Param("page") Page page,
                                       @Param("store") Store store);

    //添加仓库的方法
    public int insertStore(Store store);

    //根据仓库id修改仓库的方法
    public int updateStoreById(Store store);

    //根据仓库id删除仓库的方法
    public int deleteStoreById(Integer storeId);


    //根据仓库编码查询仓库的方法
    public Store findTypeBystoreNum(String storeNum);



}