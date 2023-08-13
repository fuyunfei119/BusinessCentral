package com.example.businesscentral.Dao.Utils;

import com.example.businesscentral.Dao.Annotation.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusinessCentralUtils {

    public static Field getPrimaryKeyField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Keys.class)) {
                Keys keys = field.getAnnotation(Keys.class);
                if (keys.PRIMARY_KEY()) {
                    return field;
                }
            }
        }

        throw new RuntimeException("Primary key field not found in class " + clazz.getName());
    }

    public static String convertToSnakeCase(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(input.charAt(0)));
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append("_");
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    public static String convertToCamelCase(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(currentChar));
                    capitalizeNext = false;
                } else {
                    result.append(currentChar);
                }
            }
        }
        return result.toString();
    }

    public static Integer CountPlaceHolders(String sqlExpression) {
        int count = 0;

        Matcher matcher = Pattern.compile("%\\d+").matcher(sqlExpression);
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public static void ParserSQLExpression(List<String> fitlers, String sqlExpression, String field, Object[] newValue) throws Exception {

        List<String> placeHolders = new ArrayList<>(Arrays.asList(sqlExpression.split("(?=[|&])|(?<=[|&])")));
        String Convert_Field_Name = field;
        int lengthOfNewValue = 0;

        String finalFilter = fitlers.contains(field) ? " AND " : "";

        for (String placeHolder : placeHolders) {
            Object currentValue = newValue[lengthOfNewValue];
            if (currentValue.getClass().equals(String.class)) {
                currentValue = "'" + currentValue + "'";
            }
            switch (placeHolder) {
                case "|" -> finalFilter = " OR ";
                case "&" -> finalFilter = " AND ";
                default -> {
                    if (placeHolder.startsWith("%") && (!placeHolder.contains("..")) && (!placeHolder.contains("*"))) {
                        fitlers.add(finalFilter + Convert_Field_Name + " = " + currentValue);
                    } else if (placeHolder.contains("..")) {
                        fitlers.add(Convert_Field_Name + " BETWEEN " + currentValue + " AND " + newValue[lengthOfNewValue + 1]);
                        lengthOfNewValue++;
                    } else {
                        String operator;
                        if (placeHolder.contains(">=")) {
                            operator = ">=";
                        } else if (placeHolder.contains("<=")) {
                            operator = "<=";
                        } else if (placeHolder.contains("<>")) {
                            operator = "<>";
                        } else if (placeHolder.contains(">")) {
                            operator = ">";
                        } else if (placeHolder.contains("<")) {
                            operator = "<";
                        } else if (placeHolder.contains("*")) {

                            int firstStar = placeHolder.indexOf("*");

                            if (placeHolder.indexOf("*", firstStar + 1) == -1) {
                                if (placeHolder.contains("%2")) {
                                    fitlers.add(Convert_Field_Name + " LIKE '" + placeHolder
                                            .replace("*", "%")
                                            .replace("%1", currentValue.toString())
                                            .replace("%2", newValue[lengthOfNewValue + 1].toString()) + "'"
                                    );
                                } else {
                                    fitlers.add(Convert_Field_Name + " LIKE '" + placeHolder
                                            .replace("*", "%")
                                            .replace("%1", currentValue.toString()) + "'"
                                    );
                                }

                            } else {
                                fitlers.add(Convert_Field_Name + " LIKE '" + placeHolder
                                        .replace("%1", currentValue.toString())
                                        .replace("*", "%") + "'"
                                );
                            }

                            lengthOfNewValue++;
                            break;

                        } else {
                            throw new Exception("IIlegalSQLExpression");
                        }

                        fitlers.add(finalFilter + Convert_Field_Name + " " + operator + " " + currentValue);
                    }
                    lengthOfNewValue++;
                }
            }
        }
    }

    public static Map<String, Object> compareObjects(Object obj1, Object obj2) throws IllegalAccessException {

        Class<?> objClass = obj1.getClass();
        Field[] fields = objClass.getDeclaredFields();

        Map<String, Object> diffMap = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj1Value = field.get(obj1);
            Object obj2Value = field.get(obj2);
            if (!Objects.equals(obj1Value, obj2Value)) {
                diffMap.put(field.getName(), obj1Value);
            }
        }

        return diffMap;
    }

    public static List<String> getFieldNameList(Object object) {
        List<String> fieldNameList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (fieldValue != null && !"".equals(fieldValue)) {
                    fieldNameList.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldNameList;
    }

    public static List<Object> getFieldValueList(Object object) {
        List<Object> fieldValueList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(object);
                if (fieldValue != null && !"".equals(fieldValue)) {
                    fieldValueList.add(fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldValueList;
    }

    public static List<String> getFieldNameList(Object object, Boolean UseFullFields) {
        if (!UseFullFields) {
            return getFieldNameList(object);
        }

        List<String> fieldNameList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                fieldNameList.add(field.getName());
            }
        }
        return fieldNameList;
    }

    public static List<Object> getFieldValueList(Object object, Boolean UseFullFields) {
        if (!UseFullFields) {
            return getFieldValueList(object);
        }

        List<Object> fieldValueList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(object);
                    fieldValueList.add(fieldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return fieldValueList;
    }
}
