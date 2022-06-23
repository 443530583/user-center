package com.app.usercenter.mode.domain.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author admin
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -5788754009554789392L;

    private String userAccount;

    private String checkPassword;

    private String userPassword;

    private String planetCode;
}
