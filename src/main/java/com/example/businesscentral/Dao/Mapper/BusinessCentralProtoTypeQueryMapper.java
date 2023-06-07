package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BusinessCentralProtoTypeQueryMapper {

    final String Query = "SELECT ${fields} FROM ${tableName}";
    final String QueryByFields = "SELECT ${field} FROM ${table}";
//    final String QueryByFilter = "SELECT * FROM ${table} ${filters}";
    final String QueryByFilter =
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

    @Select(Query)
    List<LinkedHashMap<String,Object>> FindSetByTableName(@Param("fields") String fields,@Param("tableName") String tableName);

    @Select(QueryByFields)
    List<LinkedHashMap<String,Object>> FindSetByFields(@Param("table") Object table, @Param("field") Object field);

    @Select(QueryByFilter)
    List<LinkedHashMap<String,Object>> FindSetByFilters(@Param("table") Object table,@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters);
}
