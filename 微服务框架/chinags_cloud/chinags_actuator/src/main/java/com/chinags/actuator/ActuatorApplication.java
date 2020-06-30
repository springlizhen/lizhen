package com.chinags.actuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ActuatorApplication {
    public static void main(String[] arg) {
        SpringApplication.run(ActuatorApplication.class);

    }
}
