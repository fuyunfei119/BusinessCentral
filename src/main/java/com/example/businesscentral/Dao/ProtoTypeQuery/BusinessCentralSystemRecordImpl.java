package com.example.businesscentral.Dao.ProtoTypeQuery;

import com.example.businesscentral.Dao.Annotation.*;
import com.example.businesscentral.Dao.Enum.PageType;
import com.example.businesscentral.Dao.ProtoType.PageMySql;
import com.example.businesscentral.Dao.RecordData.CustomerRecord;
import com.example.businesscentral.Dao.Request.CardField;
import com.example.businesscentral.Dao.Request.CardGroup;
import com.example.businesscentral.Dao.Scanner.BusinessCentralObjectScan;
import com.example.businesscentral.Dao.BusinessCentralSystemRecord;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeQueryMapper;
import com.example.businesscentral.Dao.Utils.BusinessCentralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

@Repository
@Scope("prototype")
public class BusinessCentralSystemRecordImpl implements BusinessCentralSystemRecord {

    @Autowired
    private BusinessCentralProtoTypeQueryMapper businessCentralProtoTypeQueryMapper;
    @Autowired
    private CustomerRecord customerRecord;
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
    public List<Object> FindSetByFields(Map<String, Object> filters) {
        Object table = filters.get("table");
        Object filterName = filters.get("filterName");

        List<LinkedHashMap<String, Object>> linkedHashMaps = businessCentralProtoTypeQueryMapper.FindSetByFields(table, filterName);

        List<Object> convertedList = new ArrayList<>();

        for (LinkedHashMap<String, Object> map : linkedHashMaps) {
            Object item = map.values().stream().findFirst().orElse(null);
            convertedList.add(item);
        }

        return convertedList;
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
            LinkedHashMap<String, CardField> map = new LinkedHashMap<>();
            cardGroup.setGroupName(groupName);

            for (Field declaredField : bean.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(PageField.class)) {
                    PageField annotation = declaredField.getAnnotation(PageField.class);
                    if (!annotation.GROUP().isBlank()) {
                        if (annotation.GROUP().equals(groupName)) {

                            CardField cardField = new CardField();
                            if (declaredField.getType().isAssignableFrom(String.class)) {
                                cardField.setType("Text");
                            }else if (declaredField.getType().isAssignableFrom(Integer.class) ||
                                    declaredField.getType().isAssignableFrom(Double.class) ||
                                    declaredField.getType().isAssignableFrom(BigDecimal.class) ||
                                    declaredField.getType().isAssignableFrom(BigInteger.class)) {
                                cardField.setType("number");
                            } else if (declaredField.getType().isAssignableFrom(Date.class)) {
                                cardField.setType("date");
                            } else if (declaredField.getType().isAssignableFrom(Time.class)) {
                                cardField.setType("time");
                            }
                            cardField.setValue("");

                            map.put(declaredField.getName(), cardField);
                        }
                    }
                }
            }

            cardGroup.setFields(map);
            cardGroups.add(cardGroup);
        }

        return cardGroups;
    }

    @Override
    @Transactional
    public LinkedHashMap<String, Object> InsertNewRecord(Map<String,Object> objectMap) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object table = objectMap.get("table");
        Object bean = applicationContext.getBean(table.toString().toLowerCase(Locale.ROOT));

        LinkedHashMap<String,Object> record = (LinkedHashMap<String, Object>) objectMap.get("record");
        LinkedHashMap<String,Object> newRecord = new LinkedHashMap<>();

        record.forEach((s, o) -> {
            if (!ObjectUtils.isEmpty(o)) {
                newRecord.put(s,o);
            }
        });

        // Raise up Init Trigger
        Object newRecordInstance = bean.getClass().getDeclaredConstructor().newInstance();
        for (Method declaredMethod : bean.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(OnInit.class)) {
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(newRecordInstance);
            }
        }

        newRecord.forEach((s, o) -> {

            LinkedHashMap<String,Object> Value = (LinkedHashMap<String, Object>) o;
            if (StringUtils.hasLength(Value.get("value").toString())) {
                try {
                    Field field = bean.getClass().getDeclaredField(s);
                    if (field.isAnnotationPresent(TableField.class)) {
                        TableField annotation = field.getAnnotation(TableField.class);
                        if (!annotation.ON_VALIDATE().isBlank()) {
                            Method TableValidateTrigger = ReflectionUtils.findMethod(bean.getClass(), annotation.ON_VALIDATE(),Object.class);
                            if (!ObjectUtils.isEmpty(TableValidateTrigger)) {

                                TableValidateTrigger.setAccessible(true);
                                Object RecordAfterTableValidate = TableValidateTrigger.invoke(bean, ((LinkedHashMap<?, ?>) o).get("value"));

                                Field FieldAfterTableValidate = RecordAfterTableValidate.getClass().getDeclaredField(field.getName());
                                FieldAfterTableValidate.setAccessible(true);
                                Object TableFieldValueAfterValidate = FieldAfterTableValidate.get(RecordAfterTableValidate);

                                Field FieldNewRecordInstance = newRecordInstance.getClass().getDeclaredField(field.getName());
                                FieldNewRecordInstance.setAccessible(true);
                                FieldNewRecordInstance.set(newRecordInstance,TableFieldValueAfterValidate);

                            }else {
                                Class<?> type = field.getType();
                                field.setAccessible(true);
                                if (type.isAssignableFrom(Integer.class)) {
                                    field.set(newRecordInstance,Integer.parseInt(((LinkedHashMap<?, ?>) o).get("value").toString()));
                                } else if (type.isAssignableFrom(Date.class)) {
                                    field.set(newRecordInstance, Date.valueOf(((LinkedHashMap<?, ?>) o).get("value").toString()));
                                } else if (type.isAssignableFrom(String.class)) {
                                    field.set(newRecordInstance,((LinkedHashMap<?, ?>) o).get("value"));
                                }
                            }
                        }else {
                            Class<?> type = field.getType();
                            field.setAccessible(true);
                            if (type.isAssignableFrom(Integer.class)) {
                                field.set(newRecordInstance,Integer.parseInt(((LinkedHashMap<?, ?>) o).get("value").toString()));
                            } else if (type.isAssignableFrom(Date.class)) {
                                field.set(newRecordInstance, Date.valueOf(((LinkedHashMap<?, ?>) o).get("value").toString()));
                            } else if (type.isAssignableFrom(String.class)) {
                                field.set(newRecordInstance,((LinkedHashMap<?, ?>) o).get("value"));
                            }
                        }
                    }else {
                        Class<?> type = field.getType();
                        field.setAccessible(true);
                        if (type.isAssignableFrom(Integer.class)) {
                            field.set(newRecordInstance,Integer.parseInt(((LinkedHashMap<?, ?>) o).get("value").toString()));
                        } else if (type.isAssignableFrom(Date.class)) {
                            field.set(newRecordInstance, Date.valueOf(((LinkedHashMap<?, ?>) o).get("value").toString()));
                        } else if (type.isAssignableFrom(String.class)) {
                            field.set(newRecordInstance,((LinkedHashMap<?, ?>) o).get("value"));
                        }
                    }
                } catch (NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println(newRecordInstance);

        List<String> fields = new ArrayList<>();

        for (Field declaredField : newRecordInstance.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object o = declaredField.get(newRecordInstance);
            if (!ObjectUtils.isEmpty(o)) {
                fields.add(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()));
            }
        }

        List<Object> values = new ArrayList<>();
        for (Field declaredField : newRecordInstance.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            if (declaredField.get(newRecordInstance) == null) {
                continue;
            }
            if (declaredField.getType().isAssignableFrom(String.class) || declaredField.getType().isAssignableFrom(Date.class)) {
                values.add("'" + declaredField.get(newRecordInstance) + "'");
            }else {
                values.add(declaredField.get(newRecordInstance));
            }
        }

        System.out.println(values);

        businessCentralProtoTypeQueryMapper.InsertNewRecord(table.toString(),fields,values);

        return null;
    }

    @Override
    public List<String> GetFilterGroups(Map<String, Object> filter) {

        List<String> fields = new ArrayList<>();

        String table = (String) filter.get("table");

        Object bean = applicationContext.getBean(table.toLowerCase(Locale.ROOT));

        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            fields.add(BusinessCentralUtils.convertToSnakeCase(declaredField.getName()));
        }

        return fields;
    }

    @Override
    public List<LinkedHashMap<String, Object>> GetDataForListPage(String table) {

        Collection<Object> beans = applicationContext.getBeansWithAnnotation(Page.class).values();

        PageMySql pageMysql = applicationContext.getBean(PageMySql.class);

        for (Object bean : beans) {
            for (Annotation annotation : bean.getClass().getAnnotations()) {
                Page page = (Page) annotation;

                if (page.SOURCETABLE().equals(table) && page.TYPE().equals(PageType.List)) {
                    Class<?> aClass = bean.getClass();
                    for (Field declaredField : aClass.getDeclaredFields()) {
                        if (declaredField.isAnnotationPresent(PageField.class)) {
                            PageField pageField = declaredField.getAnnotation(PageField.class);
                            if (pageField.VISIABLE()) {
                                pageMysql.SetLoadFields(declaredField.getName());
                            }
                        }
                    }
                }
            }
        }

        List list = pageMysql.FindSet();
        return list;
    }
}
