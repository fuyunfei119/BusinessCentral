package com.example.businesscentral.Aop;

import com.example.businesscentral.Annotation.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.Objects;


@Component
@Aspect
public class TableAOPConfig {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * <p>
     * This pointcut expression matches any method that is either a Modify, Insert, Delete or Init method of the TestRecord class
     * or any of its subclasses.
     * <p>
     * This is intended to be used for triggering table events when the data in the table is modified or manipulated in any way.
     */
//    @Pointcut("execution(* com.example.aop.Record.TestRecord+.Modify(..)) " +
//            "|| execution(* com.example.aop.Record.TestRecord*.Insert(..)) " +
//            "|| execution(* com.example.aop.Record.TestRecord*.Delete(..)) " +
//            "|| execution(* com.example.aop.Record.TestRecord*.Init(..))")
//    public void OnTriggerTableEvent() {
//    }

    @Pointcut("execution(* com.example.businesscentral.Dao.BusinessCentral+.Modify(..,Boolean)) && args(Boolean) " +
            "|| execution(* com.example.businesscentral.Dao.BusinessCentral*.Insert(..,Boolean)) " +
            "|| execution(* com.example.businesscentral.Dao.BusinessCentral*.Delete(..,Boolean)) " +
            "|| execution(* com.example.businesscentral.Dao.BusinessCentral*.Init(..,Boolean))")
    public void OnTriggerTableEvent() {
    }

    /**
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("OnTriggerTableEvent()")
    public Object OnDoTest(ProceedingJoinPoint pjp) throws Throwable {

        if (!Objects.equals(pjp.getKind(), JoinPoint.METHOD_EXECUTION)) return pjp.proceed();

        Class<?> table = GetClass(pjp);

        switch (pjp.getSignature().getName()) {
            case "Insert" -> HandleInsertTrigger(table);
            case "Modify" -> HandleModifyTrigger(table);
            case "Delete" -> HandleDeleteTrigger(table);
            case "Init" -> HandleInitTrigger(table);
        }

        return pjp.proceed();
    }

    /**
     * This method retrieves the Class object of the target object's "aClass" field from the ProceedingJoinPoint
     * And use Class.forName() method to obtain the corresponding Class object..
     * <P>
     * @param pjp pjp the ProceedingJoinPoint that contains information about the intercepted method.
     * @return the Class object corresponding to the "aClass" field of the target object.
     * @throws NoSuchMethodException if the "getaClass" method cannot be found in the target object's class.
     * @throws ClassNotFoundException if the specified class cannot be found.
     * @throws InvocationTargetException if the "getaClass" method cannot be invoked on the target object.
     * @throws IllegalAccessException if the "getaClass" method cannot be accessed due to insufficient permissions.
     */
    private Class<?> GetClass(ProceedingJoinPoint pjp) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        Object aClass = pjp.getTarget().getClass().getDeclaredMethod("getaClass").invoke(pjp.getTarget());

        String classPath = aClass.toString().replace("class ", "");

        return Class.forName(classPath);
    }


    private void HandleInsertTrigger(Class<?> table) {

        if (!table.isAnnotationPresent(OnInsert.class)) return;

        OnInsert OnInsert = Objects.requireNonNull(table.getDeclaredAnnotation(OnInsert.class));

        if (OnInsert.value().isBlank()) return;

        String methodName = OnInsert.value();

        Method method = ReflectionUtils.findMethod(table, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(table);

            ReflectionUtils.invokeMethod(method,bean);
        }

//        InvokeTriggerMethod(methodName,table);

    }

    private void HandleModifyTrigger(Class<?> table) {

        if (!table.isAnnotationPresent(OnModify.class)) return;

        OnModify onModify = Objects.requireNonNull(table.getDeclaredAnnotation(OnModify.class));

        if (onModify.value().isBlank()) return;

        String methodName = onModify.value();

        Method method = ReflectionUtils.findMethod(table, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(table);

            ReflectionUtils.invokeMethod(method,bean);
        }

//        InvokeTriggerMethod(methodName,table);

    }

    private void HandleDeleteTrigger(Class<?> table) {

        if (!table.isAnnotationPresent(OnDelete.class)) return;

        OnDelete OnDelete = Objects.requireNonNull(table.getDeclaredAnnotation(OnDelete.class));

        if (OnDelete.value().isBlank()) return;

        String methodName = OnDelete.value();

        Method method = ReflectionUtils.findMethod(table, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(table);

            ReflectionUtils.invokeMethod(method,bean);
        }

//        InvokeTriggerMethod(methodName,table);
    }

    private void HandleInitTrigger(Class<?> table) {

        if (!table.isAnnotationPresent(OnInit.class)) return;

        OnInit OnInit = Objects.requireNonNull(table.getDeclaredAnnotation(OnInit.class));

        if (OnInit.value().isBlank()) return;

        String methodName = OnInit.value();

        Method method = ReflectionUtils.findMethod(table, methodName);

        if (method != null) {

            method.setAccessible(true);

            Object bean = applicationContext.getBean(table);

            ReflectionUtils.invokeMethod(method,bean);
        }

//        InvokeTriggerMethod(methodName,table);

    }

//    private void InvokeTriggerMethod(String methodName,Class<?> table) {
//
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TableTriggerScan.class);
//
//        Collection<Object> beans = applicationContext.getBeansWithAnnotation(TableTrigger.class).values();
//
//        TriggerUtils.InvokeTableMethod(methodName,beans,table);
//
//    }

}
