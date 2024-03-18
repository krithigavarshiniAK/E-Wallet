package com.serviceImplementation.Wallet.Config;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.DatabaseReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class GeoLocationConfig {
    private static DatabaseReader databaseReader = null;
    private static ResourceLoader resourceLoader;

    public GeoLocationConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @Bean
    public DatabaseReader databaseReader(){
        try{
            log.info("GeoLocationConfig: Trying to load GeoLite2-Country database");
            Resource resource = resourceLoader.getResource("classpath:maxmind/GeoLite2-Country.mmdb");
            InputStream dbStream = resource.getInputStream();
            log.info("GeoLocationConfig: Database loaded Successfully");

            return databaseReader = new DatabaseReader
                    .Builder(dbStream)
                    .fileMode(Reader.FileMode.MEMORY)
                    .build();
        } catch (IOException | NullPointerException e) {
            log.error("Database Reader cannot be initialised", e);
            return null;
        }
    }
}
