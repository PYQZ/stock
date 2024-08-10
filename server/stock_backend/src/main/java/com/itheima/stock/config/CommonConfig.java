package com.itheima.stock.config;

import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(StockInfoConfig.class)
@Configuration
public class CommonConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PasswordEncoder passwordEncoder2(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1l,2l);
    }
}
