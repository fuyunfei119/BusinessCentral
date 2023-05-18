package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface BusinessCentralProtoTypeMapper {

    final String FindSet =
                    "<script>" +
                        "SELECT" +
                            "<if test='LoadFields.isEmpty()'> * </if>" +
                            "<if test='!(LoadFields.isEmpty())'>" +
                                "${LoadFields} "+
                            "</if>" +
                        "FROM Customer" +
                            "<if test='Filters.isEmpty()'></if>" +
                            "<if test='!(Filters.isEmpty())'>" +
                        "WHERE" +
                            "<foreach collection='Filters' item='Filter'>" +
                                "${Filter}" +
                            "</foreach>" +
                            "</if>" +
                    "</script>";


    @Select(FindSet)
    List<LinkedHashMap<String,Object>> FindSet(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters);
}
