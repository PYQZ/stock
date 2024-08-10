package com.itheima.stock.config;

import com.itheima.stock.pojo.vo.TaskThreadPoolInfo;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class TaskExecutePoolConfig {
    @Autowired
    private TaskThreadPoolInfo info;
    
    @Bean(name = "threadPoolTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(info.getCorePoolSize());
        executor.setMaxPoolSize(info.getMaxPoolSize());
        executor.setKeepAliveSeconds(info.getKeepAliveSeconds());
        executor.setQueueCapacity(info.getQueueCapacity());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
    
}
