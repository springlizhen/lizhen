package com.chinags.user;

import com.chinags.common.idgen.IdWorker;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@Api("启动类")
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker(){
        return new IdWorker(1, 1);
    }


//    @RestController
//    class AaaController {
//        @Autowired
//        DiscoveryClient discoveryClient;
//
//        @ApiOperation(value = "获取接口说明")
//        @GetMapping("/service-user")
//        public String dc() {
//            String services = "Services: " + discoveryClient.getServices();
//            System.out.println(services);
//            return services;
//        }
//    }
}
