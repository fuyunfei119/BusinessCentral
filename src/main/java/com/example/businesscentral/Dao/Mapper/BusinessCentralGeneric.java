package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BusinessCentralGeneric<T> {
    @Select("SELECT * FROM Customer")
    List<T> FindSet();
}
