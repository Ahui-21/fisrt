package com.bjh.controller;

import com.bjh.entity.ProductType;
import com.bjh.entity.Result;
import com.bjh.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/productCategory")
@RestController
public class ProductTypeController {

    //注入ProductTypeService
    @Autowired
    private ProductTypeService productTypeService;

    /**
     * 查询商品分类树的url接口/productCategory/product-category-tree
     *
     * 返回值Result对象给客户端响应查询到的所有商品分类树List<ProductType>;
     */
    @RequestMapping("/product-category-tree")
    public Result productCategoryTree(){
        //执行业务
        List<ProductType> productTypeList = productTypeService.queryAllTypeTree();
        //响应
        return Result.ok(productTypeList);
    }

    /*
     * 校验分类编码是否存在
     * */

    /**
     * 校验分类编码是否已存在的url接口/productCategory/verify-type-code
     */
    @RequestMapping("/verify-type-code")
    public Result checkTypeCode(String typeCode){
        //执行业务
        Result result = productTypeService.checkTypeByCode(typeCode);
        //响应
        return result;
    }

    /**
     * 添加商品分类的url接口/productCategory/type-add
     *
     *
     */
    @RequestMapping("/type-add")
    public Result addProductType(@RequestBody ProductType productType){
        //执行业务
        Result result = productTypeService.saveProductType(productType);
        //响应
        return result;
    }

    /*
    *  删除   type-delete/19
    * */
    @RequestMapping("/type-delete/{typeId}")
    public Result removeProductType(@PathVariable Integer typeId){
        //执行业务
        Result result = productTypeService.deleteProductType(typeId);
        //响应
        return result;
    }


    /*  修改商品分类的url接口/productCategory/type-update
     *
     */
    @RequestMapping("/type-update")
    public Result updateType(@RequestBody ProductType productType){
        //执行业务
        Result result = productTypeService.updateProductType(productType);
        //响应
        return result;
    }
}
