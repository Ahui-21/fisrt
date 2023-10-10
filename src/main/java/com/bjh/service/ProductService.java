package com.bjh.service;

import com.bjh.entity.Product;
import com.bjh.entity.Result;
import com.bjh.page.Page;

import java.util.List;

public interface ProductService{

    //分页查询商品的业务方法
    public Page queryProductPage(Page page, Product product);

    //添加商品的业务方法
    public Result saveProduct(Product product);

    //修改商品状态
    public Result updateProductStateByPid(Product product);

    //删除商品
    public Result deleteProductByIds(List<Integer> productIdList);

    //修改商品的业务方法
    public Result updateProduct(Product product);




}
