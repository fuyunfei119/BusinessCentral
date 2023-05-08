package com.example.businesscentral.Config;

import com.example.businesscentral.Annotation.FieldTrigger;
import com.example.businesscentral.Annotation.TableTrigger;
import com.example.businesscentral.Annotation.Trigger;
import com.example.businesscentral.System.SystemTriggerType;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class EntityAndTriggerScanProcessor implements BeanFactoryPostProcessor {
    private List<String> validateTriggerMethod = new ArrayList<>();
    private List<String> initTriggerMethod = new ArrayList<>();
    private List<String> insertTriggerMethod = new ArrayList<>();
    private List<String> modifyTriggerMethod = new ArrayList<>();
    private List<String> deleteTriggerMethod = new ArrayList<>();
    private String rootPackagePathWithDot;
    private String rootPackagePathWithSlash;

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Collection<Object> values = beanFactory.getBeansWithAnnotation(SpringBootApplication.class).values();

        for (Object value : values) {
            this.rootPackagePathWithDot = value.getClass().getPackage().getName();
        }

        this.rootPackagePathWithSlash = this.rootPackagePathWithDot.replace(".","/");

        Collection<Object> fieldTriggers = beanFactory.getBeansWithAnnotation(FieldTrigger.class).values();
        Collection<Object> tableTriggers = beanFactory.getBeansWithAnnotation(TableTrigger.class).values();

        this.FieldTriggerScan(fieldTriggers);
        this.TableModifyScan(tableTriggers);
    }

    private void TableModifyScan(Collection<Object> beans) throws IOException {
        for (Object bean : beans) {
            ReflectionUtils.doWithMethods(bean.getClass(), method -> {
                Trigger annotation = AnnotationUtils.findAnnotation(method, Trigger.class);

                if (ObjectUtils.isEmpty(annotation)) return;

                switch (annotation.RaisedFor()) {
                    case Init -> InitTriggerMethodsScan(method);
                    case Modify -> ModifyTriggerMethodsScan(method);
                    case Delete -> DeleteTriggerMethodsScan(method);
                    case Insert -> InsertTriggerMethodsScan(method);
                }

            });
        }

        CreateSystemEnumType("InitTriggerType");
        CreateSystemEnumType("InsertTriggerType");
        CreateSystemEnumType("ModifyTriggerType");
        CreateSystemEnumType("DeleteTriggerType");

    }

    private void InitTriggerMethodsScan(Method method) {
        this.initTriggerMethod.add(method.getName());
    }

    private void ModifyTriggerMethodsScan(Method method) {
        this.modifyTriggerMethod.add(method.getName());
    }

    private void InsertTriggerMethodsScan(Method method) {
        this.insertTriggerMethod.add(method.getName());
    }

    private void DeleteTriggerMethodsScan(Method method) {
        this.deleteTriggerMethod.add(method.getName());
    }

    private void ValidateTriggerMethodsScan(Method method) {
        this.validateTriggerMethod.add(method.getName());
    }

    private void FieldTriggerScan(Collection<Object> beans) throws IOException {
        for (Object bean : beans) {
            ReflectionUtils.doWithMethods(bean.getClass(), method -> {
                Trigger annotation = AnnotationUtils.findAnnotation(method, Trigger.class);

                if (annotation == null) return;

                if (annotation.RaisedFor() == SystemTriggerType.Validate) {
                    ValidateTriggerMethodsScan(method);
                }
            });
        }

        CreateSystemEnumType("ValidateTriggerType");

    }

    private void CreateSystemEnumType(String enumName) throws IOException {
        File directory = new File("src/main/java/"+this.rootPackagePathWithSlash +"/System");

        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, enumName + ".java");
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        PrintWriter writer = new PrintWriter(new FileWriter(file));

        writer.println("package " +  this.rootPackagePathWithDot + ".System" + ";");
        writer.println();
        writer.println("public enum " + enumName + " {");

        switch (enumName) {
            case "ValidateTriggerType":
                for (String method : validateTriggerMethod) {
                    writer.println("\t" + method + ",");
                }
                break;
            case "InitTriggerType":
                for (String method : initTriggerMethod) {
                    writer.println("\t" + method + ",");
                }
                break;
            case "InsertTriggerType":
                for (String method : insertTriggerMethod) {
                    writer.println("\t" + method + ",");
                }
                break;
            case "ModifyTriggerType":
                for (String method : modifyTriggerMethod) {
                    writer.println("\t" + method + ",");
                }
                break;
            case "DeleteTriggerType":
                for (String method : deleteTriggerMethod) {
                    writer.println("\t" + method + ",");
                }
                break;
        }

        writer.println("}");
        writer.close();
    }
}
