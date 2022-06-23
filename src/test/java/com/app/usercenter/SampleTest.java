package com.app.usercenter;

import com.alibaba.fastjson.JSONArray;
import com.app.usercenter.mapper.UserMapper;
import com.app.usercenter.mode.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static com.app.usercenter.utils.PasswordDecoderUtil.getMD5Password;

/**
 * @program: user-center
 * @description
 * @author: 郑向旺
 * @create: 2022-06-04 01:22
 **/

/**
 * 1.使用 org.junit.Test 进行测试试，不会装配SpringBoot 环境，需要用org.junit.jupiter.api.Test
 * 2.如果要使用org.junit.Test，需要加上@RunWith(SpringRunner.class) 进行上下文环境配置
 */
@SpringBootTest()
//@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper mapper;


    @Test
    public void testSelect() {
        System.out.println("-------");
//        List<UserTest> users = mapper.selectList(null);
        List<User> users = mapper.selectList(null);
//        Assert.assertEquals(0, users.size());
//        users.forEach(System.out::println);
        System.out.println(JSONArray.toJSONString(users));
    }


    @Test
    void test(){
        String decodePassword = getMD5Password("123123");
        System.out.println(decodePassword);
    }
}
