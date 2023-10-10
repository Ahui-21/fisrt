package com.bjh.mapper;

import com.bjh.entity.ProductType;

import java.util.List;

public interface ProductTypeMapper {

    //查询商品分类
     public  List<ProductType> selectAllProductType();

     //根据分类编码查询
     public ProductType selectTypeByCodeOrName(ProductType productType);

    //添加商品分类的方法
    public int insertProductType(ProductType productType);

    //删除商品
    public int deleteProductType(Integer typeId);

    //修改商品
    public int updateProductType(ProductType productType);

}