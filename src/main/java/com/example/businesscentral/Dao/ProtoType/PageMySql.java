package com.example.businesscentral.Dao.ProtoType;

import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public class PageMySql<T,E extends Enum<E>> implements BusinessCentralPage<T,E> {

    @Autowired
    private BusinessCentralProtoTypeMapper businessCentralProtoTypeMapper;
    private List<String> filters = new ArrayList<>();
    private List<String> loadfilters = new ArrayList<>();

    public PageMySql(BusinessCentralProtoTypeMapper businessCentralProtoTypeMapper) {
        this.businessCentralProtoTypeMapper = businessCentralProtoTypeMapper;
    }

    @Override
    public BusinessCentralPage<T, E> Reset() {

        this.filters = new ArrayList<>();
        this.loadfilters = new ArrayList<>();

        return this;
    }

    @Override
    public BusinessCentralPage<T,E> SetLoadFields(E field) throws NoSuchFieldException {

        this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(field.name()));

        return this;
    }

    @Override
    public BusinessCentralPage<T, E> SetLoadFields(String field) {

        this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(field));

        return this;
    }

    @Override
    public BusinessCentralPage<T,E> SetRange(E field, Object Value) throws NoSuchFieldException {

        if (!this.filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(BusinessCentralUtils.convertToSnakeCase(field.name()) + " = " + "'" +Value + "'");

        return this;
    }

    @Override
    public BusinessCentralPage<T,E> SetFilter(E field, String sqlExpression, Object... Value) {
        return null;
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSet() {
        return businessCentralProtoTypeMapper.FindSet(String.join(", ", loadfilters), filters);
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindFirst(Boolean Prototype) {
        return businessCentralProtoTypeMapper.FindFirst(String.join(", ", loadfilters), filters);
    }

    @Override
    public LinkedHashMap<String, Object> FindLast(Boolean Prototype) {
        return businessCentralProtoTypeMapper.FindLast(String.join(", ", loadfilters), filters);
    }

    @Override
    public List<LinkedHashMap<String, Object>> Find(Integer Count, Boolean Prototype) {
        return businessCentralProtoTypeMapper.Find(String.join(", ", loadfilters), filters, Count);
    }

    @Override
    public LinkedHashMap<String, Object> Get(Object ID, Boolean Prototype) {
        return businessCentralProtoTypeMapper.Get(String.join(", ", loadfilters), filters);
    }
}
