package com.bjh.service.impl;

import com.bjh.entity.Result;
import com.bjh.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.bjh.entity.Product;
import com.bjh.mapper.ProductMapper;
import com.bjh.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    //分页查询商品的业务方法
    @Override
    public Page queryProductPage(Page page, Product product) {

        //查询行数
        Integer count = productMapper.selectProductCount(product);
        //分页查询
        List<Product> productList = productMapper.selectProductPage(page,product);

        page.setTotalNum(count);
        page.setResultList(productList);
        return page;
    }


    /*
    * 添加商品
    * */
    @Value("${file.access-path}")
    private String fileAccessPath;
    @Override
    public Result saveProduct(Product product) {

        Product productByNum = productMapper.findProductByNum(product.getProductNum());

        if (productByNum != null){
            return Result.err(Result.CODE_ERR_BUSINESS,"已存在！");
        }

        //处理上传图片的路径】
        product.setImgs(fileAccessPath+product.getImgs());

        int i = productMapper.insertProduct(product);
        if (i >0){
            return Result.ok("添加商品成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "添加商品失败！");

    }

    /*
    * 修改商品状态
    * */
    @Override
    public Result updateProductStateByPid(Product product) {

        int ret = productMapper.updateProductStateByPid(product);

        if (ret > 0){
            return Result.ok("修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改失败！");
    }



    /*
     * 删除商品
     * */
    @Override
    public Result deleteProductByIds(List<Integer> productIdList) {

        int ret = productMapper.deleteProductByIds(productIdList);

        if (ret > 0){
            return Result.ok("删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "删除失败！");
    }


    /*
    * 修改商品
    * */
    @Override
    public Result updateProduct(Product product) {

        //判断修改后商品型号是否已存在
        Product productByNum = productMapper.findProductByNum(product.getProductNum());
        if (productByNum!=null && !productByNum.getProductId().equals(product.getProductId())){//商品型号修改且修改后的型号已存在

            return Result.err(Result.CODE_ERR_BUSINESS,"修改的商品型号已存在！");
        }

        //判断上传图片是否被修改
        if (!product.getImgs().contains(fileAccessPath)){
            product.setImgs(fileAccessPath+product.getImgs());
        }

        //执行修改
        int i = productMapper.updateProduct(product);
        if(i>0){
            return Result.ok("商品修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"商品修改失败！");
    }

}
