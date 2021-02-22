package com.atguigu.gmall.auth;

import com.atguigu.gmall.auth.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableConfigurationProperties({JwtProperties.class})
public class GmallAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallAuthApplication.class, args);
    }

}
