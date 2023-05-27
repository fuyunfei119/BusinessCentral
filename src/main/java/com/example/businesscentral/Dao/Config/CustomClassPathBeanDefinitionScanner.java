package com.example.businesscentral.Dao.Config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    public CustomClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isInterface();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {

            ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();

            String beanClassName = beanDefinition.getBeanClassName();

            Class<?> aClass;
            Class<?> aClass1 = null;
            Class<?> aClass2 = null;

            try {
                aClass = Class.forName(beanClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            for (Type genericInterface : aClass.getGenericInterfaces()) {

                if (genericInterface instanceof ParameterizedType parameterizedType) {

                    aClass1 = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                    aClass2 = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                }

            }

            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();

            List<Class<?>> classes = new ArrayList<>();
            classes.add(aClass);
            classes.add(aClass1);
            classes.add(aClass2);

            argumentValues.addIndexedArgumentValue(0,classes);

            beanDefinition.setConstructorArgumentValues(argumentValues);

            beanDefinition.setBeanClass(BusinessCentralFactoryBean.class);
        }

        return beanDefinitionHolders;
    }
}
