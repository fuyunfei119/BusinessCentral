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
    final String QueryByFilter = "SELECT * FROM ${table} ${filters}";

    @Select(Query)
    List<LinkedHashMap<String,Object>> FindSetByTableName(@Param("fields") String fields,@Param("tableName") String tableName);

    @Select(QueryByFields)
    List<LinkedHashMap<String,Object>> FindSetByFields(@Param("table") Object table, @Param("field") Object field);

    @Select(QueryByFilter)
    List<LinkedHashMap<String,Object>> FindSetByFilters(@Param("table") Object table,@Param("filters") String filters);
}
