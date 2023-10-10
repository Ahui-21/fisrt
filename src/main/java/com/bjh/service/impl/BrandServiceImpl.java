package com.bjh.service.impl;

import com.bjh.entity.Brand;
import com.bjh.mapper.BrandMapper;
import com.bjh.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "com.bjh.service.impl.BrandServiceImpl")
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    /*
    * //查询所有品牌的方法
    * */
    @CacheEvict(key = "'all:brand'")
    @Override
    public List<Brand> queryAllBrand() {
        return brandMapper.findAllBrand();
    }
}
