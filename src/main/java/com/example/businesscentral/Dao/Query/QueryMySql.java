package com.example.businesscentral.Dao.Query;

import com.example.businesscentral.Dao.BusinessCentralQuery;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Scope(value = "prototype")
public class QueryMySql<E extends Enum<E>> implements BusinessCentralQuery<E> {

    @Autowired
    private BusinessCentralMapper businessCentralMapper;
    private List<String> Joins = new ArrayList<>();
    private List<String> loadFields = new ArrayList<>();
    private Class<?> aClass;
    private String tableName;

    @Override
    public BusinessCentralQuery<E> SetJoinCondition(E... field) {

        Arrays.stream(field).map(e -> this.Joins.add(e.name()));

        return this;
    }

    @Override
    public List<String> GetJoinCondition() {
        this.Joins = this.Joins.stream().map(s -> tableName + "." + s).toList();

        return this.Joins;
    }

    @Override
    public BusinessCentralQuery<E> SetDataItem(E... field) {

        this.loadFields = Arrays.stream(field).map(Enum::name).toList();
        return this;
    }

    @Override
    public List<String> GetDataItem() {
        return this.loadFields;
    }

    @Override
    public <T> BusinessCentralQuery<E> GetDataSet(Class<T> classes) {

        assert this.loadFields != null : "load fields can not be null";

        this.aClass = classes;
        this.tableName = aClass.getSimpleName();

        this.loadFields = this.loadFields.stream().map(s -> tableName + "." + convertFirstUppercaseToUnderscore(s)).toList();

        return this;
    }


    private static String convertFirstUppercaseToUnderscore(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                String prefix = str.substring(0, i);
                String suffix = str.substring(i + 1);
                return prefix + "_" + Character.toLowerCase(c) + suffix;
            }
        }

        return str;
    }
}
