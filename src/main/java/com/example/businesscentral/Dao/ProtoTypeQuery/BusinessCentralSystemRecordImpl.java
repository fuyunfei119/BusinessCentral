package com.example.businesscentral.Dao.ProtoTypeQuery;

import com.example.businesscentral.Dao.Annotation.Table;
import com.example.businesscentral.Dao.Scanner.BusinessCentralObjectScan;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeQueryMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Scope("prototype")
public class BusinessCentralSystemRecordImpl implements BusinessCentralSystemRecord {

    @Autowired
    private BusinessCentralProtoTypeQueryMapper businessCentralProtoTypeQueryMapper;

    @Override
    public List<LinkedHashMap<String, Object>> FindSetByTableName(String TableName) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BusinessCentralObjectScan.class);

        Set<Map.Entry<String, Object>> entries = applicationContext.getBeansWithAnnotation(Table.class).entrySet();

        List<? extends Class<?>> collect = entries.stream()
                .filter(bean -> bean.getKey().equalsIgnoreCase(TableName))
                .map(bean -> bean.getValue().getClass())
                .toList();

        Class<?> aClass = collect.get(0);

        List<String> list = Arrays.stream(aClass.getDeclaredFields())
                .map(field -> BusinessCentralUtils.convertToSnakeCase(field.getName()))
                .toList();

        return businessCentralProtoTypeQueryMapper.FindSetByTableName(String.join(",",list),TableName);
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSetByFields(Map<String,Object> filters) {
        Object table = filters.get("table");
        Object filterName = filters.get("filterName");
        return businessCentralProtoTypeQueryMapper.FindSetByFields(table,filterName);
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) {

        List<String> finalfilters = new ArrayList<>();
        StringBuilder finalConditions = new StringBuilder();

        Object table = filters.get("table");
        Map<String,Object> conditions = (Map<String, Object>) filters.get("filters");
        for (String key : conditions.keySet()) {
            if (!finalConditions.toString().equals("")) {
                finalConditions.append(" AND ").append(key).append(" = '").append(conditions.get(key)).append("'");
            }else {
                String value = conditions.get(key).toString();
                List<String> placeHolders = new ArrayList<>(Arrays.asList(conditions.get(key).toString().split("(?=[|&])|(?<=[|&])")));
                List<Object> filterValues = new ArrayList<>();
                int index = 0;

                for (String placeHolder : placeHolders) {
                    if (!placeHolder.equals("&")
                            && !placeHolder.equals("..")
                            && !placeHolder.equals("|")
                            && !placeHolder.equals("**")
                            && !placeHolder.equals("*")
                            && !placeHolder.equals(">")
                            && !placeHolder.equals(">=")
                            && !placeHolder.equals("<")
                            && !placeHolder.equals("<=")
                            && !placeHolder.equals("<>")
                            && !placeHolder.equals("=")
                    ) {
                        filterValues.add(placeHolders.get(index));
                        placeHolders.set(index,"%"+index);
                    }

                    index++;
                }

                System.out.println("Placeholder" + placeHolders);
                System.out.println("newValues" + filterValues);


                finalConditions.append(" WHERE ").append(key).append(" = '").append(conditions.get(key)).append("'");
            }
        }

//        BusinessCentralUtils.ParserSQLExpression(finalfilters,);

//        System.out.println(finalConditions.toString());

//        return businessCentralProtoTypeQueryMapper.FindSetByFilters(table,finalConditions.toString());
        return null;
    }
}
