package com.example.businesscentral.Dao.ProtoType;

import com.example.businesscentral.Annotation.PK;
import com.example.businesscentral.Dao.BusinessCentralProtoType;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Scope("prototype")
public class ProtoTypeMySql<T extends Enum<T>> implements BusinessCentralProtoType<T> {

    @Autowired
    private BusinessCentralProtoTypeMapper businessCentralProtoTypeMapper;
    private final List<String> filters = new ArrayList<>();
    private final List<String> loadfilters = new ArrayList<>();
    private Class<?> aClass;

    @Override
    public BusinessCentralProtoType<T> SetPrimaryKey(T field) throws Exception {

        String classPath = field.getClass().getName().replaceAll("\\$Fields$", "");

        if (ObjectUtils.isEmpty(classPath)) throw new Exception("IIegal Class Type.");

        Class<?> className = Class.forName(classPath);

        if (className.getDeclaredField(field.name()).isAnnotationPresent(PK.class)) {
            this.aClass = className;
        }

        return this;
    }

    @Override
    public BusinessCentralProtoType<T> SetLoadFields(T field) throws NoSuchFieldException {

        Field declaredField = aClass.getDeclaredField(field.name());

        this.loadfilters.add(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()));

        return this;
    }

    @Override
    public BusinessCentralProtoType<T> SetRange(T field, Object Value) throws NoSuchFieldException {

        Field declaredField = aClass.getDeclaredField(field.name());

        if (!this.filters.isEmpty()) {
            filters.add(" AND ");
        }

        filters.add(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()) + " = " + "'" +Value + "'");

        return this;
    }

    @Override
    public BusinessCentralProtoType<T> SetFilter(T field, String sqlExpression, Object... Value) {
        return null;
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSet() {
        return businessCentralProtoTypeMapper.FindSet(String.join(", ", loadfilters), filters);
    }
}
