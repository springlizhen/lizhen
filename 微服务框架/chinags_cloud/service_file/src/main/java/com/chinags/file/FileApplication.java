package com.chinags.file;

import com.chinags.common.idgen.IdWorker;
import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@Api("启动类")
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class);
    }

    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }
    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("102400MB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("1024000MB");
        return factory.createMultipartConfig();
    }

}
