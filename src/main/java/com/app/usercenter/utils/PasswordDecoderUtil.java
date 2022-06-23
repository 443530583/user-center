package com.app.usercenter.utils;

import org.springframework.util.DigestUtils;

/**
 * @author  密码加密工具类
 * @date 2022/5/29 14:21
 */
public class PasswordDecoderUtil {
    /**
     * 盐值
     */
    public static final String SALT = "JAVA";
    /**
     * 加密次数
     */
    public static final Integer COUNT = 3;

    /**
     * 循环MD5加密
     *
     * @param password
     * @return
     */
    public static String getMD5Password(String password) {
        String salt = "{{" + SALT + "}}";
        String digest = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        // 做一个三次循环MD5加密
        for (int x = 0; x < COUNT; x++) {
            digest = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        }
        return digest;
    }

}
