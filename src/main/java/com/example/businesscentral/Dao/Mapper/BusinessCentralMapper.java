package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BusinessCentralMapper<T> {

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

    final String IsEmpty =
            "<script>" +
                    "SELECT EXISTS("+
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
                    "LIMIT 1" +
                    ")"+
                    "</script>";

    final String FindFirst =
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
                    "LIMIT 1"+
                    "</script>";

    final String FindLast =
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
                    "FROM Customer" +
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
                    "FROM Customer" +
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
                    "FROM Customer" +
                    "<if test='Filters.isEmpty()'></if>" +
                    "<if test='!(Filters.isEmpty())'>" +
                    "WHERE" +
                    "<foreach collection='Filters' item='Filter'>" +
                    "${Filter}" +
                    "</foreach>" +
                    "</if>" +
                    "</script>";

    final String Insert =
            "<script>" +
                    "INSERT INTO Customer" +
                    "<foreach collection='Fields' item='Field' open='(' separator=',' close=')' >"+
                    "${Field}"+
                    "</foreach>"+
                    "VALUES"+
                    "<foreach collection='Values' item='Value' open='(' separator=',' close=')' >"+
                    "#{Value}"+
                    "</foreach>" +
                    "</script>";

    final String InsertWithFullField =
            "<script>" +
                    "INSERT INTO Customer " +
                    "<foreach collection='Fields' item='Field' open='(' separator=',' close=')' >"+
                    "${Field}"+
                    "</foreach>" +
                    "VALUES"+
                    "<foreach collection='FullValues' item='FullValue' open='(' separator=',' close=')' >"+
                    "<if test='FullValue == null'> null </if>"+
                    "<if test='FullValue != null'> '${FullValue}' </if>"+
                    "</foreach>" +
                    "</script>";

    final String Delete =
            "DELETE FROM Customer WHERE ${PK_Field} = #{PK_Value}";

    final String Modify =
            "<script>"+
                    "UPDATE Customer SET" +
                    "<foreach collection='DiffMap' index='Field' item='Value' separator=','>" +
                    "${Field} = #{Value}" +
                    "</foreach>" +
                    "WHERE" +
                    " ${PK_Field} = #{PK_Value}"+
                    "</script>";


    @Select(FindSet)
    List<T> FindSet(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters);

    @Select(IsEmpty)
    Integer IsEmpty(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters);

    @Select(FindFirst)
    T FindFirst(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters);

    @Select(FindLast)
    T FindLast(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters);

    @Select(Find)
    List<T> Find(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters,@Param("Count") Integer Count);

    @Select(Get)
    T Get(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters);

    @Select(Count)
    Integer Count(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters);

    @Insert(Insert)
    Integer Insert(@Param("Fields") List<String> Fields,@Param("Values") List<Object> Values);

    @Insert(InsertWithFullField)
    Integer InsertWithFullField(@Param("Fields") List<String> Fields,@Param("FullValues") List<Object> FullValues);

    @Delete(Delete)
    Integer Delete(@Param("PK_Field") String PrimaryKey,@Param("PK_Value") Object PrimaryKeyValue);

    @Update(Modify)
    Integer Modify(@Param("DiffMap") Map<String, Object> DiffMap, @Param("PK_Field") String Field, @Param("PK_Value") Object Value);

}
