package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DevTestApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DevTestApplication.class);
        //No need to start the default Tomcat embedded server
        springApplication.setWebEnvironment(false);
        ConfigurableApplicationContext context = springApplication.run(args);
    }
}
