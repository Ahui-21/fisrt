package com.bjh.mapper;

import com.bjh.entity.Supply;

import java.util.List;

public interface SupplyMapper {
    //查询所有供应商
   public List<Supply> selectAll();
}