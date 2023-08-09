package com.example.businesscentral.Dao.Mapper;

import org.apache.ibatis.annotations.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BusinessCentralMapper {

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
                    ")"+
                    "</script>";

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

    final String Insert =
            "<script>" +
                    "INSERT INTO ${table}" +
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
                    "INSERT INTO ${table} " +
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
            "DELETE FROM ${table} WHERE ${PK_Field} = ${PK_Value}";

    final String Modify =
            "<script>"+
                    "UPDATE ${table} SET" +
                    "<foreach collection='DiffMap' index='Field' item='Value' separator=','>" +
                    "${Field} = #{Value}" +
                    "</foreach>" +
                    "WHERE" +
                    " ${PK_Field} = #{PK_Value}"+
                    "</script>";


    @Select(FindSet)
    List<LinkedHashMap<String,Object>> FindSet(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters, @Param("table") String table);

    @Select(IsEmpty)
    Integer IsEmpty(@Param("LoadFields") String LoadFields, @Param("Filters") List<String> Filters, @Param("table") String table);

    @Select(FindFirst)
    LinkedHashMap<String,Object> FindFirst(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String table);

    @Select(FindLast)
    LinkedHashMap<String,Object> FindLast(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String table);

    @Select(Find)
    List<LinkedHashMap<String,Object>> Find(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters,@Param("Count") Integer Count,@Param("table") String table);

    @Select(Get)
    LinkedHashMap<String,Object> Get(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String table);

    @Select(Count)
    Integer Count(@Param("LoadFields") String LoadFields,@Param("Filters") List<String> Filters, @Param("table") String table);

    @Insert(Insert)
    Integer Insert(@Param("Fields") List<String> Fields,@Param("Values") List<Object> Values, @Param("table") String table);

    @Insert(InsertWithFullField)
    Integer InsertWithFullField(@Param("Fields") List<String> Fields,@Param("FullValues") List<Object> FullValues, @Param("table") String table);

    @Delete(Delete)
    Integer Delete(@Param("PK_Field") String PrimaryKey,@Param("PK_Value") Object PrimaryKeyValue, @Param("table") String table);

    @Update(Modify)
    Integer Modify(@Param("DiffMap") Map<String, Object> DiffMap, @Param("PK_Field") String Field, @Param("PK_Value") Object Value, @Param("table") String table);

}
