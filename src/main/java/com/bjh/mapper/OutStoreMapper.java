package com.bjh.mapper;

import com.bjh.entity.OutStore;
import com.bjh.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutStoreMapper {

    //出库
    public  int insertOutStore(OutStore outStore);
    //查询出库单总行数的方法
    public int outStoreCount(OutStore outStore);

    //分页查询出库单的方法
    public List<OutStore> outStorePage(@Param("page") Page page,
                                       @Param("outStore") OutStore outStore);

    //根据id将出库单状态改为已出库的方法
    public int updateIsOutById(Integer outsId);

}

