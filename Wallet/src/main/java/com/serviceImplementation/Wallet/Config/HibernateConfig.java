package com.serviceImplementation.Wallet.Config;

import jakarta.annotation.PostConstruct;
import ogs.switchon.common.hibernate_loader.HibernateSessionFactoryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@PropertySource("classpath:Switch.Properties")
public class HibernateConfig {
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Switch.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    @PostConstruct
    public void init() {
        Properties properties = hibernateProperties();
        HibernateSessionFactoryHelper.loadProperties(properties, "com.serviceImplementation.Wallet.model");
    }
}
