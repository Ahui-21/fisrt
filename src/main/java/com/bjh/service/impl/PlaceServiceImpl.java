package com.bjh.service.impl;

import com.bjh.entity.Place;
import com.bjh.mapper.PlaceMapper;
import com.bjh.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@CacheConfig(cacheNames = "com.bjh.service.impl.PlaceServiceImpl")
@Service
public class PlaceServiceImpl implements PlaceService {
    //注入PlaceMapper
    @Autowired
    private PlaceMapper placeMapper;

    /*
      查询所有产地的业务方法
     */
    //对查询到的所有产地进行缓存,缓存到redis的键为all:place
    @Cacheable(key = "'all:place'")
    @Override
    public List<Place> queryAllPlace() {
        //查询所有产地
        return placeMapper.findAllPlace();
    }

}
