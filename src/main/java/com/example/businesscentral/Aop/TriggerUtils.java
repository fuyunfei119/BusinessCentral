package com.example.businesscentral.Aop;

import com.example.businesscentral.Annotation.FieldTrigger;
import com.example.businesscentral.Annotation.TableTrigger;
import com.example.businesscentral.Annotation.Trigger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;

public class TriggerUtils {

    public static void InvokeTableMethod(String methodName, Collection<Object> beans, Class<?> table) {

        boolean found = false;

        for (Object bean : beans) {

            TableTrigger tableTrigger = bean.getClass().getDeclaredAnnotation(TableTrigger.class);

            if (!tableTrigger.CLASS().equals(table)) break;

            Method[] methods = ReflectionUtils.getDeclaredMethods(bean.getClass());

            for (Method method : methods) {

                if (!method.isAnnotationPresent(Trigger.class)) continue;

                if (method.isAnnotationPresent(Trigger.class) && method.getName().equals(methodName)) {
                    ReflectionUtils.invokeMethod(method, bean);
                    found = true;
                    break;
                }
            }

            if (found) break;
        }
    }

    public static void InvokeFieldMethod(String methodName, Collection<Object> beans, Class<?> table) {

        boolean found = false;

        for (Object bean : beans) {

            FieldTrigger fieldTrigger = bean.getClass().getDeclaredAnnotation(FieldTrigger.class);

            if (!fieldTrigger.tablename().equals(table)) break;

            Method[] methods = ReflectionUtils.getDeclaredMethods(bean.getClass());

            for (Method method : methods) {

                if (!method.isAnnotationPresent(Trigger.class)) continue;

                if (method.isAnnotationPresent(Trigger.class) && method.getName().equals(methodName)) {
                    ReflectionUtils.invokeMethod(method, bean);
                    found = true;
                    break;
                }
            }

            if (found) break;
        }
    }

}
