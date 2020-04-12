package com.xuecheng.filesystem.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FastConfigTest {

    @Autowired
    private FastDFSConfig fastDFSConfig;

    @Test
    public void test01(){
        String str = fastDFSConfig.toString();
    }
}
