package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.*;

@Mapper
public interface BusinessCentralProtoTypeQueryMapper {

    final String Query = "SELECT ${fields} FROM ${tableName}";
    final String QueryByFields = "SELECT ${field} FROM ${table}";
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
    final String QueryBySearch =
            "<script>" +
                    "SELECT" +
                    "<if test='LoadFields.isEmpty()'> * </if>" +
                    "<if test='!(LoadFields.isEmpty())'>" +
                    "${LoadFields} "+
                    "</if>" +
                    "FROM Customer" +
                    "<if test='Filters.isEmpty()'></if>" +
                    "<if test='!(Filters.isEmpty())'>" +
                    "WHERE " +
                    "${Filters}" +
                    "</if>" +
                    "</script>";
    final String QueryBySort =
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
                    "<if test='Sort.isEmpty()'></if>" +
                    "<if test='!(Sort.isEmpty())'>" +
                    "${Sort}" +
                    "</if>" +
                    "</script>";
    final String GetRecordById =
            "<script>" +
                    "SELECT" +
                    "<if test='LoadFields.isEmpty()'> * </if>" +
                    "<if test='!(LoadFields.isEmpty())'>" +
                    "${LoadFields} "+
                    "</if>" +
                    "FROM Customer" +
                    " WHERE " +
                    "${Key} = '${recordID}'" +
                    " LIMIT 1" +
                    "</script>";
    final String InsertNewRecord =
            "<script>" +
                    "INSERT INTO ${table}" +
                    "<foreach collection='fields' item='field' separator=',' open='(' close=')' >" +
                    "${field}" +
                    "</foreach>" +
                    "VALUES" +
                    "<foreach collection='values' item='value' separator=',' open='(' close=')' >" +
                    "${value}" +
                    "</foreach>" +
                    "</script>";

    @Select(Query)
    List<LinkedHashMap<String,Object>> FindSetByTableName(@Param("fields") String fields,@Param("tableName") String tableName);

    @Select(QueryByFields)
    List<LinkedHashMap<String,Object>> FindSetByFields(@Param("table") Object table, @Param("field") Object field);

    @Select(QueryByFilter)
    List<LinkedHashMap<String,Object>> FindSetByFilters(@Param("table") Object table,@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters);

    @Select(QueryBySearch)
    List<LinkedHashMap<String,Object>> FindSetBySearch(@Param("table") Object table,@Param("LoadFields") String LoadFields, @Param("Filters") String Filters);

    @Select(QueryBySort)
    List<LinkedHashMap<String, Object>> FindSetByFilterAndSort(@Param("table") Object table,@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters,@Param("Sort") String sort);

    @Select(GetRecordById)
    LinkedHashMap<String, Object> GetRecordById(@Param("recordID") String recordID,@Param("LoadFields") String LoadFields,@Param("Key") String primaryKey);

    @Insert(InsertNewRecord)
    Integer InsertNewRecord(@Param("table") String tableName,@Param("fields") List<String> strings,@Param("values") Collection<Object> values);

}
