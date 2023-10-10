package com.bjh.service.impl;

import com.bjh.entity.ProductType;
import com.bjh.entity.Result;
import com.bjh.mapper.ProductMapper;
import com.bjh.mapper.ProductTypeMapper;
import com.bjh.service.ProductTypeService;
import javafx.embed.swing.JFXPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.objenesis.instantiator.perc.PercInstantiator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@CacheConfig(cacheNames = "com.bjh.service.impl.ProductTypeServiceImpl")
@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    //查询所有商品分类树
    @CacheEvict("'all:typeTree'")
    @Override
    public List<ProductType> queryAllTypeTree() {

        //查询所有分类
        List<ProductType> productTypeList = productTypeMapper.selectAllProductType();
        //将所有商品转成商品分类树

        List<ProductType> treeTypeList = allTypeToTypeTree(productTypeList, 0);
        return treeTypeList;
    }

    /*
     * 校验分类编码是否存在
     * */
    @CacheEvict("'all:typeTree'")
    @Override
    public Result checkTypeByCode(String typeCode) {

        ProductType productType = new ProductType();
        productType.setTypeCode(typeCode);
        ProductType prodType = productTypeMapper.selectTypeByCodeOrName(productType);

        return Result.ok(prodType == null);
    }

    /*
      添加商品分类的业务方法

      @CacheEvict(key = "'all:typeTree'")清除所有商品分类树的缓存;
     */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result saveProductType(ProductType productType) {

        //检验分类名称是否存在
        ProductType prodType = new ProductType();
        prodType.setTypeName(productType.getTypeName());
        ProductType proCategory = productTypeMapper.selectTypeByCodeOrName(prodType);
        if (proCategory != null) {
            return Result.err(Result.CODE_ERR_BUSINESS, "分类名称已存在！");
        }
        //添加商品分类
        int i = productTypeMapper.insertProductType(productType);
        if (i > 0) {
            return Result.ok("分类添加成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "分类添加失败！");
    }

    /*
     * 根据分类id删除分类及其所有子级分类
     * */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result deleteProductType(Integer typeId) {
        int i = productTypeMapper.deleteProductType(typeId);
        if (i > 0) {
            return Result.ok("分类删除成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "分类删除失败！");
    }

    /*
     *
     * 修改商品
     * */
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result updateProductType(ProductType productType) {

        ProductType prodType = new ProductType();
        prodType.setTypeName(productType.getTypeName());
        ProductType type = productTypeMapper.selectTypeByCodeOrName(prodType);
        if (type != null && !type.getTypeId().equals(productType.getTypeId())) {
            return Result.err(Result.CODE_ERR_BUSINESS, "修改的分类名字已存在！");
        }

        //修改
        int i = productTypeMapper.updateProductType(productType);
        if (i > 0) {
            return Result.ok("分类修改成功！");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "分类修改失败！");
    }











    //将所有商品转成商品分类树的递归算法
    private List<ProductType> allTypeToTypeTree(List<ProductType> typeList,Integer pid){

        //拿到一级分类
        List<ProductType> firstLevelType = new ArrayList<>();
        for (ProductType productType: typeList){

            if (productType.getParentId()==pid){
                firstLevelType.add(productType);
            }
        }

        //查询一级分类下的二级分类
        for (ProductType productType : firstLevelType){
            List<ProductType> secondLevelType = allTypeToTypeTree(typeList,productType.getTypeId());
            productType.setChildProductCategory(secondLevelType);
        }


        return  firstLevelType;

    }
}
