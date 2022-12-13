package com.scnsoft.casino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BetApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(BetApplication.class, args);
    }
}
