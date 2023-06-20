package com.example.businesscentral.Dao.ProtoTypeQuery;

import com.example.businesscentral.Dao.Annotation.Keys;
import com.example.businesscentral.Dao.Annotation.PageField;
import com.example.businesscentral.Dao.Annotation.Table;
import com.example.businesscentral.Dao.Annotation.TableField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Request.SortParameter;
import com.example.businesscentral.Dao.Scanner.BusinessCentralObjectScan;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeQueryMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Repository
@Scope("prototype")
public class BusinessCentralSystemRecordImpl implements BusinessCentralSystemRecord {

    @Autowired
    private BusinessCentralProtoTypeQueryMapper businessCentralProtoTypeQueryMapper;
    @Autowired
    private ApplicationContext applicationContext;

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

        return businessCentralProtoTypeQueryMapper.FindSetByTableName(String.join(",", list), TableName);
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSetByFields(Map<String, Object> filters) {
        Object table = filters.get("table");
        Object filterName = filters.get("filterName");
        return businessCentralProtoTypeQueryMapper.FindSetByFields(table, filterName);
    }

    @Override
    public List<LinkedHashMap<String, Object>> FindSetByFilters(Map<String, Object> filters) throws Exception {

        List<String> finalfilters = new ArrayList<>();

        Object table = filters.get("table");
        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));

        Map<String, Object> conditions = (Map<String, Object>) filters.get("filters");

        for (String key : conditions.keySet()) {

            List<Object> placeHolders = new ArrayList<>(Arrays.asList(conditions.get(key).toString().split("(?=[|&])|(?<=[|&])")));
            List<Object> filterValues = new ArrayList<>();
            int index = 0;

            for (Object placeHolder : placeHolders) {
                if (!placeHolder.equals("&") && !placeHolder.equals("|")) {
                    if (placeHolder.toString().contains("..")) {
                        String expression = placeHolder.toString();
                        String left = expression.substring(0, expression.indexOf(".."));
                        String right = expression.substring(expression.indexOf("..") + 2, expression.length());
                        filterValues.add(left);
                        filterValues.add(right);
                        placeHolder = placeHolder.toString().replace(left, "%1");
                        placeHolder = placeHolder.toString().replace(right, "%2");

                    } else if (placeHolder.toString().contains("*")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<>") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains(">")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf(">") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains(">=")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf(">=") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<=")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<=") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<>")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<>") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else {
                        filterValues.add(placeHolders.get(index));
                        placeHolders.set(index, "%" + index);
                    }
                }
                index++;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (Object placeHolder : placeHolders) {
                stringBuilder.append(placeHolder);
            }

            if (!finalfilters.isEmpty()) {
                finalfilters.add(" AND ");
            }

            finalfilters.add("(");

            Field declaredField = bean.getClass().getDeclaredField(BusinessCentralUtils.convertToCamelCase(key));
            Class<?> type = declaredField.getType();
            if (type.equals(Integer.class)) {
                for (int i = 0; i < filterValues.size(); i++) {
                    filterValues.set(i, Integer.parseInt(filterValues.get(i).toString()));
                }
            }

            BusinessCentralUtils.ParserSQLExpression(finalfilters, stringBuilder.toString(), key, filterValues.toArray());

            finalfilters.add(")");
        }

        return businessCentralProtoTypeQueryMapper.FindSetByFilters(table, "", finalfilters);
    }

    @Override
    public List<LinkedHashMap<String, Object>> QueryContent(Map<String, Object> filters) {
        StringBuilder finalFilters = new StringBuilder();

        Object table = filters.get("table");
        String content = (String) filters.get("content");
        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));

        if (content.contains("*")) {
            if (content.startsWith("*") && content.endsWith("*")) {
                for (Field declaredField : bean.getClass().getDeclaredFields()) {
                    content = content.replace("*","");
                    finalFilters
                            .append(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()))
                            .append(" LIKE ")
                            .append("'%")
                            .append(content)
                            .append("%'")
                            .append(" OR ");
                }
            } else if (content.startsWith("*")) {
                for (Field declaredField : bean.getClass().getDeclaredFields()) {
                    content = content.replace("*","");
                    finalFilters
                            .append(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()))
                            .append(" LIKE ")
                            .append("'%")
                            .append(content)
                            .append("' OR ");
                }
            } else if (content.endsWith("*")) {
                for (Field declaredField : bean.getClass().getDeclaredFields()) {
                    content = content.replace("*","");
                    finalFilters
                            .append(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()))
                            .append(" LIKE '")
                            .append(content)
                            .append("%'")
                            .append(" OR ");
                }

            }

            finalFilters.delete(finalFilters.lastIndexOf(" OR "),finalFilters.length());

            return businessCentralProtoTypeQueryMapper.FindSetBySearch(table, "", finalFilters.toString());
        }else {
            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                finalFilters
                        .append(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()))
                        .append(" LIKE ")
                        .append("'%")
                        .append(content)
                        .append("%'")
                        .append(" OR ");
            }

            finalFilters.delete(finalFilters.lastIndexOf(" OR "),finalFilters.length());

            return businessCentralProtoTypeQueryMapper.FindSetBySearch(table, "", finalFilters.toString());
        }
    }

    @Override
    public List<LinkedHashMap<String, Object>> SortLinesByDescending(Map<String, Object> filters) throws Exception {

        List<String> finalfilters = new ArrayList<>();

        Object table = filters.get("table");
        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));

        Map<String, Object> conditions = (Map<String, Object>) filters.get("filters");

        for (String key : conditions.keySet()) {

            List<Object> placeHolders = new ArrayList<>(Arrays.asList(conditions.get(key).toString().split("(?=[|&])|(?<=[|&])")));
            List<Object> filterValues = new ArrayList<>();
            int index = 0;

            for (Object placeHolder : placeHolders) {
                if (!placeHolder.equals("&") && !placeHolder.equals("|")) {
                    if (placeHolder.toString().contains("..")) {
                        String expression = placeHolder.toString();
                        String left = expression.substring(0, expression.indexOf(".."));
                        String right = expression.substring(expression.indexOf("..") + 2, expression.length());
                        filterValues.add(left);
                        filterValues.add(right);
                        placeHolder = placeHolder.toString().replace(left, "%1");
                        placeHolder = placeHolder.toString().replace(right, "%2");

                    } else if (placeHolder.toString().contains("*")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<>") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains(">")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf(">") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains(">=")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf(">=") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<=")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<=") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else if (placeHolder.toString().contains("<>")) {
                        String expression = placeHolder.toString();
                        String value = expression.substring(expression.indexOf("<>") + 1, expression.length());
                        placeHolder = placeHolder.toString().replace(value, "%1");
                        filterValues.add(value);

                    } else {
                        filterValues.add(placeHolders.get(index));
                        placeHolders.set(index, "%" + index);
                    }
                }
                index++;
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (Object placeHolder : placeHolders) {
                stringBuilder.append(placeHolder);
            }

            if (!finalfilters.isEmpty()) {
                finalfilters.add(" AND ");
            }

            finalfilters.add("(");

            Field declaredField = bean.getClass().getDeclaredField(BusinessCentralUtils.convertToCamelCase(key));
            Class<?> type = declaredField.getType();
            if (type.equals(Integer.class)) {
                for (int i = 0; i < filterValues.size(); i++) {
                    filterValues.set(i, Integer.parseInt(filterValues.get(i).toString()));
                }
            }

            BusinessCentralUtils.ParserSQLExpression(finalfilters, stringBuilder.toString(), key, filterValues.toArray());

            finalfilters.add(")");
        }

        StringBuilder finalSort = new StringBuilder();
        finalSort.append(" ORDER BY ");

        LinkedHashMap<String,String> sortParam = (LinkedHashMap<String, String>) filters.get("sort");
        finalSort
                .append(sortParam.get("field"))
                .append(" ")
                .append(sortParam.get("sort"));

        System.out.println(finalSort.toString());

        return businessCentralProtoTypeQueryMapper.FindSetByFilterAndSort(table, "", finalfilters,finalSort.toString());
    }

    @Override
    public LinkedHashMap<String, Object> GetRecordById(Map<String, Object> filters) {

        Object table = filters.get("table");
        Object recordID = filters.get("RecordID");
        String PrimaryKey = "";

        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));
        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Keys.class)) {
                Keys annotation = declaredField.getAnnotation(Keys.class);
                if (annotation.PRIMARY_KEY()) {
                    PrimaryKey = BusinessCentralUtils.convertToSnakeCase(declaredField.getName());
                }
            }
        }

        return businessCentralProtoTypeQueryMapper.GetRecordById(recordID.toString(),"",PrimaryKey);
    }

    @Override
    public List<CardGroup> GetAllFieldNames(Map<String,String> table) {

        String tableName = table.get("table");
        List<String> GroupNames = new ArrayList<>();
        List<CardGroup> cardGroups = new ArrayList<>();

        Object bean = applicationContext.getBean(tableName.toLowerCase(Locale.ROOT));

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(PageField.class)) {
                PageField annotation = declaredField.getAnnotation(PageField.class);
                if (!annotation.GROUP().isBlank()) {
                    if (!GroupNames.contains(annotation.GROUP())) {
                        GroupNames.add(annotation.GROUP());
                    }
                }
            }
        }

        for (String groupName : GroupNames) {

            CardGroup cardGroup = new CardGroup();
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            cardGroup.setGroupName(groupName);

            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(PageField.class)) {
                    PageField annotation = declaredField.getAnnotation(PageField.class);
                    if (!annotation.GROUP().isBlank()) {
                        if (annotation.GROUP().equals(groupName)) {
                            map.put(declaredField.getName(), "");
                        }
                    }
                }
            }

            cardGroup.setFields(map);
            cardGroups.add(cardGroup);
        }

        return cardGroups;
    }
}
