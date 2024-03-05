package com.serviceImplementation.Wallet.Config;

import com.serviceImplementation.Wallet.model.Wallet;
import ogs.switchon.common.hibernate_loader.CommonSessionFactoryHelper;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Component
@PropertySource("classpath:SwitchPropertiesDev.txt")
public class ExternalPropertyConfig {
    public ExternalPropertyConfig() {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("SwitchPropertiesDev.txt")) {
                if (is != null) {
                    Properties hibernateProperties = new Properties();
                    hibernateProperties.load(is);
                    System.out.println(" property is loaded.....");

                    Set<Class<?>> hibernateClasses = new HashSet<>();
                    hibernateClasses.add(Wallet.class);
                    System.out.println("hibernate classes are added.....");

                    Configuration configuration = new Configuration();
                    System.out.println("configurations are made");
                    SessionFactory sessionFactory = CommonSessionFactoryHelper.getMetadata(hibernateProperties, hibernateClasses, configuration);
                } else {
                    throw new RuntimeException("Switch.properties not found in classpath");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



