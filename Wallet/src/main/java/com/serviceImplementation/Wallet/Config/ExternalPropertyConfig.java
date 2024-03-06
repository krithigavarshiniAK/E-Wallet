package com.serviceImplementation.Wallet.Config;

import ogs.switchon.common.hibernate_loader.HibernateSessionFactoryHelper;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ExternalPropertyConfig {

    public ExternalPropertyConfig() {
        System.out.println("ExternalPropertyConfig bean is being created.");
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream is = HibernateSessionFactoryHelper.class.getClassLoader().getResourceAsStream("SwitchPropertiesDev.txt")) {
            if (is != null) {
                System.out.println("Inside if..");
                properties.load(is);
                properties.forEach((key, value) -> System.out.println(key + ": " + value));

            } else {
                throw new RuntimeException("Property file not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }
        System.out.println("Out of try catch");
        HibernateSessionFactoryHelper.loadProperties(properties);
        System.out.println("properties are loaded......");
    }
    public Session getSession() {
        return HibernateSessionFactoryHelper.getSession();
    }
}



