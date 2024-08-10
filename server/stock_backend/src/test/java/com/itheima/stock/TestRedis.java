package com.itheima.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class TestRedis {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Test
    public void test01(){
        redisTemplate.opsForValue().set("name","张三");
        String name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
}
