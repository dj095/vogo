package com.kalaari.util;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * DIGVIJAY | CONTEXT_HOLDER CAN FETCH ANY BEAN DEPENDENCY FROM THE SPRING
 * CONTAINER AT RUNTIME FOR INJECTION.
 */
@Slf4j
@Component
@ComponentScan
public class ContextHolder implements ApplicationContextAware, EnvironmentAware {

    private static ContextHolder _instance;
    private ApplicationContext applicationContext;
    private Environment environment;

    public static ContextHolder getInstance() {
        return _instance;
    }

    public static <T> T getBean(Class<T> tClass) {
        return _instance.getApplicationContext().getBean(tClass);
    }

    public static <T> T getBeanById(String beanId, Class<T> tClass) {
        return _instance.getApplicationContext().getBean(beanId, tClass);
    }

    public static <T> void registerSingleton(String beanName, T bean) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) ContextHolder.getInstance()
                .getApplicationContext()).getBeanFactory();
        beanFactory.registerSingleton(beanName, bean);
    }

    public static <T> void destroySingleton(Class<T> tClass) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) ContextHolder.getInstance()
                .getApplicationContext()).getBeanFactory();
        ((DefaultListableBeanFactory) beanFactory).destroySingleton(tClass.getCanonicalName());
    }

    public static <T> void removeBeanDefinitionByName(String beanName) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) ContextHolder.getInstance()
                .getApplicationContext()).getBeanFactory();
        ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(beanName);
    }

    public static void printAllBeans() {
        log.info("PRINTING ALL BEANS");
        log.info(String
                .valueOf(Arrays.asList(ContextHolder.getInstance().getApplicationContext().getBeanDefinitionNames())));

    }

    public static String getBeanNameForClass(Class<?> clazz) {
        String fullyQualifiedClassName = clazz.getName();
        String[] dotSplittedClassName = fullyQualifiedClassName.split("\\.");
        String classActualName = dotSplittedClassName[dotSplittedClassName.length - 1];
        String firstLetter = classActualName.substring(0, 1).toLowerCase();
        String restActualName = classActualName.substring(1, classActualName.length());
        return firstLetter + restActualName;
    }

    public static <T> T getService(Class<T> tClass) {
        return getBean(tClass);
    }

    public static Environment getEnvironment() {
        return _instance.environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        _instance = this;
    }
}
