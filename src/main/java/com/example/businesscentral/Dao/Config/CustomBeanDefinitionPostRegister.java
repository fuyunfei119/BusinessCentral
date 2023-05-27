package com.example.businesscentral.Dao.Config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Repository;

@Configuration
public class CustomBeanDefinitionPostRegister implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        CustomClassPathBeanDefinitionScanner scanner = new CustomClassPathBeanDefinitionScanner(beanDefinitionRegistry);

        scanner.addIncludeFilter(new AssignableTypeFilter(Repository.class));

        scanner.scan("com/example/businesscentral/Dao/PageData","com/example/businesscentral/Dao/RecordData");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
