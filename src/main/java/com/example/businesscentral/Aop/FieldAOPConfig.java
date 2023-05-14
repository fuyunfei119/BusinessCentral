package com.example.businesscentral.Aop;

import com.example.businesscentral.Annotation.OnValidate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class FieldAOPConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(* com.example.businesscentral.Dao.BusinessCentral.Validate(..))")
    public void OnValidate() {}

    @Around("OnValidate()")
    public Object OnDoTest(ProceedingJoinPoint pjp) throws Throwable {

        if (!Objects.equals(pjp.getKind(), JoinPoint.METHOD_EXECUTION)) return pjp.proceed();

        Object arg = pjp.getArgs()[0];

        Class<?> table = GetClass(arg);

        Field field = table.getDeclaredField(arg.toString());

        HandleValidateTrigger(field,table);

        return pjp.proceed();
    }

    private Class<?> GetClass(Object arg) throws ClassNotFoundException {

        String classpath = arg.getClass().getName().replace("$Fields", "");

        return Class.forName(classpath);
    }

    private void HandleValidateTrigger(Field field,Class<?> table) throws InvocationTargetException, IllegalAccessException {

        if (!field.isAnnotationPresent(OnValidate.class)) return;

        OnValidate onValidate = Objects.requireNonNull(field.getAnnotation(OnValidate.class));

        if (onValidate.value().isBlank()) return;

        String methodName = onValidate.value();

        Method method = ReflectionUtils.findMethod(table, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(table);

            ReflectionUtils.invokeMethod(method,bean);
        }
    }
}
