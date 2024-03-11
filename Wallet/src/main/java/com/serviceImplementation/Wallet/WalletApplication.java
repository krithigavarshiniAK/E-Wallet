package com.serviceImplementation.Wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@PropertySource("classpath:Config.Properties")
@PropertySource("classpath:SwitchPropertiesDev.txt")
public class WalletApplication
{
    public WalletApplication() {
        System.out.println("1");
    }

    public static void main(String[] args)
	{
		SpringApplication.run(WalletApplication.class, args);
	}
}