package com.xuecheng.auth.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void testGet(){
        stringRedisTemplate.boundValueOps("name").set("zhangsan");
        String name = stringRedisTemplate.boundValueOps("name").get();
        System.out.println(name);


    }


}
