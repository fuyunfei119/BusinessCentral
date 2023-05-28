package com.example.businesscentral.Dao.Config;

import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Mapper.BusinessCentralMapper;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.ProtoType.PageMySql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessCentralBeanProxy implements InvocationHandler {

    @Autowired
    private PageMySql pageMySql;
    @Autowired
    private BusinessCentralRecordMySql businessCentralRecordMySql;
    @Autowired
    private ApplicationContext applicationContext;
    private Class<?> entityClass;


    public BusinessCentralBeanProxy(ApplicationContext applicationContext, List<Class<?>> classes) {
        if (ObjectUtils.isEmpty(classes)) return;

        this.applicationContext = applicationContext;
        this.entityClass = classes.get(0);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getDeclaringClass().equals(BusinessCentralRecord.class)) {
            if (ObjectUtils.isEmpty(businessCentralRecordMySql)) {

                List<Class<?>> classes = new ArrayList<>();
                classes.add(this.entityClass);
                businessCentralRecordMySql = new BusinessCentralRecordMySql<>(applicationContext,classes);
            }

            return method.invoke(businessCentralRecordMySql, args);
        }

        if (method.getDeclaringClass().equals(BusinessCentralPage.class)) {
            if (ObjectUtils.isEmpty(pageMySql)) {

                BusinessCentralProtoTypeMapper bean = applicationContext.getBean(BusinessCentralProtoTypeMapper.class);

                pageMySql = new PageMySql<>(bean);
            }

            return method.invoke(pageMySql, args);
        }

        return null;
    }
}
