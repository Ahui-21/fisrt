package com.bjh.mapper;

import com.bjh.entity.Product;
import com.bjh.page.Page;
import org.apache.ibatis.annotations.Param;

import javax.naming.InsufficientResourcesException;
import java.util.List;

public interface ProductMapper {
    //查询商品总行数的方法
    public Integer selectProductCount(Product product);

    //分页查询商品的方法
    public List<Product> selectProductPage(@Param("page") Page page,
                                           @Param("product") Product product);

    //型号查询
    public Product findProductByNum(String productNum);
    //添加商品的方法
    public int insertProduct(Product product);

    //修改商品状态
    public int updateProductStateByPid(Product product);

    //删除商品
    public int deleteProductByIds(List<Integer> productIdList);

    //修改商品
    public int updateProduct(Product product);

    //根据商品id增加商品库存的方法
    public int addInventById(Integer productId, Integer invent);

    //根据商品id查询商品的方法
    public Product selectProductById(Integer productId);


}