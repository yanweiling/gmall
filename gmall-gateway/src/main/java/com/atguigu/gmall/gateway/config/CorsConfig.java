package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){

        CorsConfiguration config=new CorsConfiguration();
        //允许的域，不要写*,否则cookie就不起作用了
        config.addAllowedOrigin("http://localhost:1000");
        //允许的头信息
        config.addAllowedHeader("*");
        //允许的请求方式
        config.addAllowedMethod("*");
        //设置是否允许设置cookie
        config.setAllowCredentials(true);
        //添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(corsConfigurationSource);
    }
}
