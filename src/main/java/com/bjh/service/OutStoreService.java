package com.bjh.service;


import com.bjh.entity.OutStore;
import com.bjh.entity.Result;
import com.bjh.page.Page;

public interface OutStoreService{

    //添加出库单的业务方法
    public Result saveOutStore(OutStore outStore);
    //分页查询出库单的业务方法
    public Page outStorePage(Page page, OutStore outStore);

    //确定出库的业务方法
    public Result confirmOutStore(OutStore outStore);

}

