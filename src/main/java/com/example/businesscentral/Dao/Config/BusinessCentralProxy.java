package com.example.businesscentral.Dao.Config;

import com.example.businesscentral.Dao.BusinessCentralPage;
import com.example.businesscentral.Dao.BusinessCentralRecord;
import com.example.businesscentral.Dao.Impl.BusinessCentralRecordMySql;
import com.example.businesscentral.Dao.Mapper.BusinessCentralProtoTypeMapper;
import com.example.businesscentral.Dao.ProtoType.PageMySql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Component
public class BusinessCentralProxy implements InvocationHandler {

    @Autowired
    private PageMySql pageMySql;
    @Autowired
    private ApplicationContext applicationContext;

    public BusinessCentralProxy(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getDeclaringClass().equals(BusinessCentralRecord.class)) {
            return method.invoke(new BusinessCentralRecordMySql<>(), args);
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
