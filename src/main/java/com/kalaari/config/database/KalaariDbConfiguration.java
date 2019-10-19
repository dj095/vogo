package com.kalaari.config.database;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.kalaari.repository.base" },
        entityManagerFactoryRef = KalaariDbConfiguration.EntityManagerFactoryBeanName,
        transactionManagerRef = KalaariDbConfiguration.TransactionManagerBeanName)
public class KalaariDbConfiguration {

    private static final String prefixName = "database.kalaari";
    public static final String DataSourceInitializerBean = prefixName + ".data-initializer";
    // BEAN NAMES
    public static final String DataSourceBeanName = prefixName + ".data-source";
    public static final String JpaVendorAdapterBeanName = prefixName + ".jpa-vendor-adapter";
    public static final String JpaPropertyBeanName = prefixName + ".jpa-property";
    public static final String EntityManagerFactoryBeanName = prefixName + ".entity-manager-factory";
    public static final String TransactionManagerBeanName = prefixName + ".transaction-manager";
    private static final String persistentUnitName = "postgres";
    private static final String entityPackagePath = "com.kalaari.entity.db";
    @Autowired
    private Environment env;

    @Primary
    @Bean(name = DataSourceBeanName)
    @ConfigurationProperties(prefix = prefixName + ".datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(TransactionManagerBeanName)
    public PlatformTransactionManager transactionManager(
            @Qualifier(EntityManagerFactoryBeanName) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(EntityManagerFactoryBeanName)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(DataSourceBeanName) DataSource dataSource,
            @Qualifier(JpaVendorAdapterBeanName) JpaVendorAdapter vendorAdapter,
            @Qualifier(JpaPropertyBeanName) Properties jpaProperties) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setPackagesToScan(entityPackagePath);
        entityManagerFactoryBean.setPersistenceUnitName(persistentUnitName);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean;
    }

    @Primary
    @Bean(JpaVendorAdapterBeanName)
    @ConfigurationProperties(prefix = prefixName + ".jpa")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Primary
    @Bean(JpaPropertyBeanName)
    @ConfigurationProperties(prefix = prefixName + ".jpa.hibernate")
    public Properties jpaProperties() {
        Properties properties = new Properties();
        String hbm2ddlPropertyName = prefixName + ".jpa.hibernate.mode";
        if (env.containsProperty(hbm2ddlPropertyName)) {
            properties.put(AvailableSettings.HBM2DDL_AUTO, env.getProperty(hbm2ddlPropertyName));
        }
        return properties;
    }
}
