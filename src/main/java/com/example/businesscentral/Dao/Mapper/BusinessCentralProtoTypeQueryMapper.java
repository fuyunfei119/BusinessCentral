package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
public interface BusinessCentralProtoTypeQueryMapper {

    final String Query = "SELECT ${fields} FROM ${tableName}";
    final String QueryByFilter = "SELECT ${field} FROM ${table}";

    @Select(Query)
    List<LinkedHashMap<String,Object>> FindSetByTableName(@Param("fields") String fields,@Param("tableName") String tableName);

    @Select(QueryByFilter)
    List<LinkedHashMap<String,Object>> FindSetByFilter(@Param("table") Object table,@Param("field") Object field);
}
