package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
@Component
public interface BusinessCentralProtoTypeMapper {

    final String FindSet =
                    "<script>" +
                        "SELECT" +
                            "<if test='LoadFields.isEmpty()'> * </if>" +
                            "<if test='!(LoadFields.isEmpty())'>" +
                                "${LoadFields} "+
                            "</if>" +
                        "FROM ${table}" +
                            "<if test='Filters.isEmpty()'></if>" +
                            "<if test='!(Filters.isEmpty())'>" +
                        "WHERE" +
                            "<foreach collection='Filters' item='Filter'>" +
                                "${Filter}" +
                            "</foreach>" +
                            "</if>" +
                    "</script>";

    final String IsEmpty =
                "<script>" +
                    "SELECT EXISTS("+
                        "SELECT" +
                            "<if test='LoadFields.isEmpty()'> * </if>" +
                            "<if test='!(LoadFields.isEmpty())'>" +
                                "${LoadFields} "+
                            "</if>" +
                        "FROM ${table}" +
                            "<if test='Filters.isEmpty()'></if>" +
                            "<if test='!(Filters.isEmpty())'>" +
                        "WHERE" +
                            "<foreach collection='Filters' item='Filter'>" +
                                "${Filter}" +
                            "</foreach>" +
                            "</if>" +
                        "LIMIT 1" +
                    ")"+ "</script>";

    final String FindFirst =
            "<script>" +
                    "SELECT" +
                        "<if test='LoadFields.isEmpty()'> * </if>" +
                        "<if test='!(LoadFields.isEmpty())'>" +
                            "${LoadFields} "+
                        "</if>" +
                    "FROM ${table}" +
                        "<if test='Filters.isEmpty()'></if>" +
                        "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                        "<foreach collection='Filters' item='Filter'>" +
                            "${Filter}" +
                        "</foreach>" +
                        "</if>" +
                    "LIMIT 1"+
                    "</script>";

    final String FindLast =
            "<script>" +
                    "SELECT" +
                        "<if test='LoadFields.isEmpty()'> * </if>" +
                        "<if test='!(LoadFields.isEmpty())'>" +
                            "${LoadFields} "+
                        "</if>" +
                    "FROM ${table}" +
                        "<if test='Filters.isEmpty()'></if>" +
                        "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                        "<foreach collection='Filters' item='Filter'>" +
                            "${Filter}" +
                        "</foreach>" +
                        "</if>" +
                    "ORDER BY User_ID DESC "+
                    "LIMIT 1"+
                    "</script>";

    final String Find =
            "<script>" +
                    "SELECT" +
                        "<if test='LoadFields.isEmpty()'> * </if>" +
                        "<if test='!(LoadFields.isEmpty())'>" +
                            "${LoadFields} "+
                        "</if>" +
                    "FROM ${table}" +
                        "<if test='Filters.isEmpty()'></if>" +
                        "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                        "<foreach collection='Filters' item='Filter'>" +
                            "${Filter}" +
                        "</foreach>" +
                        "</if>" +
                    "ORDER BY User_ID DESC "+
                    "LIMIT ${Count}"+
                    "</script>";

    final String Get =
            "<script>" +
                    "SELECT" +
                        "<if test='LoadFields.isEmpty()'> * </if>" +
                        "<if test='!(LoadFields.isEmpty())'>" +
                            "${LoadFields} "+
                        "</if>" +
                    "FROM ${table}" +
                        "<if test='Filters.isEmpty()'></if>" +
                        "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                        "<foreach collection='Filters' item='Filter'>" +
                            "${Filter}" +
                        "</foreach>" +
                        "</if>" +
                    "LIMIT 1"+
                    "</script>";

    final String Count =
            "<script>" +
                    "SELECT" +
                        "<if test='LoadFields.isEmpty()'> Count(*) </if>" +
                        "<if test='!(LoadFields.isEmpty())'>" +
                            " Count(${LoadFields}) "+
                        "</if>" +
                    "FROM ${table}" +
                        "<if test='Filters.isEmpty()'></if>" +
                        "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                        "<foreach collection='Filters' item='Filter'>" +
                            "${Filter}" +
                        "</foreach>" +
                        "</if>" +
                    "</script>";


    @Select(FindSet)
    List<LinkedHashMap<String,Object>> FindSet(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters, @Param("table") String tableName);

    @Select(IsEmpty)
    Integer IsEmpty(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters, @Param("table") String tableName);

    @Select(FindFirst)
    List<LinkedHashMap<String,Object>> FindFirst(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String tableName);

    @Select(FindLast)
    LinkedHashMap<String,Object> FindLast(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String tableName);

    @Select(Find)
    List<LinkedHashMap<String,Object>> Find(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters,@Param("Count") Integer Count, @Param("table") String tableName);

    @Select(Get)
    LinkedHashMap<String,Object> Get(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String tableName);

    @Select(Count)
    Integer Count(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String tableName);
}
