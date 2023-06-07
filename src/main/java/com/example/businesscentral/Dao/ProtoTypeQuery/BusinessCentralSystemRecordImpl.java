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
    public List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) throws Exception {

        List<String> finalfilters = new ArrayList<>();
        StringBuilder finalConditions = new StringBuilder();

        Object table = filters.get("table");
        Map<String,Object> conditions = (Map<String, Object>) filters.get("filters");

        for (String key : conditions.keySet()) {
            if (!finalConditions.toString().equals("")) {
                finalConditions.append(" AND ").append(key).append(" = '").append(conditions.get(key)).append("'");
            }else {

                List<Object> placeHolders = new ArrayList<>(Arrays.asList(conditions.get(key).toString().split("(?=[|&])|(?<=[|&])")));
                List<Object> filterValues = new ArrayList<>();
                int index = 0;

                for (Object placeHolder : placeHolders) {
                    if (!placeHolder.equals("&") && !placeHolder.equals("|")) {
                        if (placeHolder.toString().contains("..")) {
                            String expression = placeHolder.toString();
                            String left = expression.substring(0, expression.indexOf(".."));
                            String right = expression.substring(expression.indexOf("..")+2, expression.length());
                            filterValues.add(left);
                            filterValues.add(right);
                            placeHolder = placeHolder.toString().replace(left,"%1");
                            placeHolder = placeHolder.toString().replace(right,"%2");
                        } else if (placeHolder.toString().contains("*")) {
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf("<>")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);

                        }else if (placeHolder.toString().contains(">")) {
                            System.out.println(placeHolder);
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf(">")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);
                        }else if (placeHolder.toString().contains(">=")) {
                            System.out.println(placeHolder);
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf(">=")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);
                        }else if (placeHolder.toString().contains("<")) {
                            System.out.println(placeHolder);
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf("<")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);
                        }else if (placeHolder.toString().contains("<=")) {
                            System.out.println(placeHolder);
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf("<=")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);

                        }else if (placeHolder.toString().contains("<>")) {
                            System.out.println(placeHolder);
                            String expression = placeHolder.toString();
                            String value = expression.substring(expression.indexOf("<>")+1, expression.length());
                            placeHolder = placeHolder.toString().replace(value,"%1");
                            filterValues.add(value);

                        }
                        else {
                            filterValues.add(placeHolders.get(index));
                            placeHolders.set(index,"%"+index);
                        }
                    }
                    index++;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (Object placeHolder : placeHolders) {
                    stringBuilder.append(placeHolder);
                }

                BusinessCentralUtils.ParserSQLExpression(finalfilters,stringBuilder.toString(),key,filterValues.toArray());

                System.out.println(finalfilters);
            }
        }

        return businessCentralProtoTypeQueryMapper.FindSetByFilters(table,"",finalfilters);
    }
}
