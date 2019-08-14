package com.imdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args)  {
        if (System.getProperty("serviceName") == null || System.getProperty("serviceName").isEmpty()) {
            System.setProperty("serviceName", "fuse-service");
        }
        SpringApplication.run(Application.class, args);
    }

}

