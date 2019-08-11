package com.xisan517.community;

import com.xisan517.community.mapper.UserMapper;
import com.xisan517.community.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunityApplicationTests {

    @Test
    public void contextLoads() {


    }
    @Autowired
    UserMapper userMapper;
    @Test
    public void testUser(){

    }

}
