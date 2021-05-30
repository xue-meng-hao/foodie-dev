package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 用于解决跨域问题
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {

    }

    @Bean
    public CorsFilter corsFilter() {
        //添加cors配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://localhost:8080");
//        corsConfiguration.addAllowedOrigin("http://192.168.17.128");
//        corsConfiguration.addAllowedOrigin("http://192.168.17.128:8080");
        corsConfiguration.addAllowedOrigin("*");
        //设置是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);
        //设置请求的方式
        corsConfiguration.addAllowedMethod("*");
        //设置允许的header
        corsConfiguration.addAllowedHeader("*");

        //为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();

        corsSource.registerCorsConfiguration("/**", corsConfiguration);
        //返回新的filter
        return new CorsFilter(corsSource);
    }

}
