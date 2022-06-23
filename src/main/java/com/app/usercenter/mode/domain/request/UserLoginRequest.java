package com.app.usercenter.mode.domain.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author admin
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1016478847765207685L;

    private String userAccount;

    private String userPassword;
}
