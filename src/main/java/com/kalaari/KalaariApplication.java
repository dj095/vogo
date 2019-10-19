package com.kalaari;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration(value = "com.kalaari")
@ComponentScan(value = "com.kalaari")
@SpringBootApplication(exclude = OAuth2AutoConfiguration.class)
@EnableRetry
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class KalaariApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KalaariApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}