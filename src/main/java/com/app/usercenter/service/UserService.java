package com.app.usercenter.service;

import com.app.usercenter.mode.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author admin
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2022-06-04 17:10:32
*/
public interface UserService extends IService<User> {


    /**
     *用户注册
     * @param userAccount   账户
     * @param checkPassword 密码
     * @param userPassword  校验码
     * @return 用户id
     */
    long userRegister(String userAccount,String checkPassword,String userPassword,String planetCode);

    /**
     * 登录
     * @param userAccount   账户
     * @param userPassword 密码
     * @return  用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return 脱敏用户信息
     */
    User getSafeUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    void userLogout(HttpServletRequest request);
}
