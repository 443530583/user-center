package com.app.usercenter.service;
import java.util.Date;

import com.app.usercenter.mode.domain.User;
import com.app.usercenter.utils.PasswordDecoderUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;


    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("zxw1");
        user.setAvatarurl("https://img1.baidu.com/it/u=1419487417,273038776&fm=253&fmt=auto&app=120&f=JPEG?w=900&h=500");
        user.setGender(0);
        user.setUserpassword(PasswordDecoderUtil.getMD5Password("123456"));
        user.setPhone("");
        user.setEmail("");
        user.setUserstatus(0);
        user.setCreatetime(new Date());
        user.setIsDelete(0);
        boolean save = userService.save(user);
        System.out.println(user.getId());
        assertTrue(save);
    }

    @Test
    void userRegister() {
        String userAccount = "123123";
        String checkPassword = "1234567";
        String userPassword = "1234567";
        String planetCode = "1234567";
        long userId = userService.userRegister(userAccount, checkPassword, userPassword,planetCode);
        System.out.println(userId);
    }
}