package com.bjh.mapper;

import com.bjh.entity.Place;

import java.util.List;

public interface PlaceMapper {
    //查询所有产地
    public List<Place> findAllPlace();

}