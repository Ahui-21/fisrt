package com.bjh.service;

import com.bjh.entity.ProductType;
import com.bjh.entity.Result;

import java.util.List;

public interface ProductTypeService {
    //查询所有商品分类树

    public List<ProductType> queryAllTypeTree();

    //根据分类编码或者分类名称查询
    public Result checkTypeByCode(String typeCode);

    //添加商品分类的业务方法
    public Result saveProductType(ProductType productType);

    //删除商品
    public Result deleteProductType(Integer typeId);

    //修改商品分类的业务方法
    public Result updateProductType(ProductType productType);


}
