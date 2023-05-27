package com.example.businesscentral.Dao.Config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.List;

@Component
public class BusinessCentralFactoryBean implements FactoryBean {

    @Autowired
    ApplicationContext applicationContext;

    Class<?> repositryInterface;
    Class<?> entityInterface;
    Class<?> fieldInterface;

    @Autowired
    public BusinessCentralFactoryBean(List<Class<?>> classes) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (classes.isEmpty()) return;

        System.out.println(classes);

        this.repositryInterface = classes.get(0);
        this.entityInterface = classes.get(1);
        this.fieldInterface = classes.get(2);
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(
                repositryInterface.getClassLoader(),
                new Class<?>[]{repositryInterface},
                new BusinessCentralBeanProxy(applicationContext)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return repositryInterface;
    }
}
