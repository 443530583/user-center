package com.app.usercenter.controller;

import com.app.usercenter.common.BaseResponse;
import com.app.usercenter.common.ErrorCode;
import com.app.usercenter.common.ResultUtils;
import com.app.usercenter.exception.BusinessException;
import com.app.usercenter.mode.domain.User;
import com.app.usercenter.mode.domain.request.UserLoginRequest;
import com.app.usercenter.mode.domain.request.UserRegisterRequest;
import com.app.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.app.usercenter.constant.UserConstant.USER_ADMIN;
import static com.app.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Slf4j
@RequestMapping("/user")
/**
 * 用户接口
 *
 * @author zxw
 */ public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户注册
     * @param userRegisterRequest 前端参数
     * userAccount   用户账户
     * password      用户密码
     * planetCode    星球编号
     * @return       用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userPassword = userRegisterRequest.getUserPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long id = userService.userRegister(userAccount, checkPassword, userPassword, planetCode);
        return id > 0 ? ResultUtils.success(id) : ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }


    /**
     * 登录
     * @param userLoginRequest      前端登录请求实体
     * @param httpServletRequest    请求
     * @return                      用户信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            log.debug("【登录IP】:" + httpServletRequest.getLocalAddr());
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = userService.doLogin(userAccount, userPassword, httpServletRequest);
        BaseResponse<User> userBaseResponse = user != null ? ResultUtils.success(user) : ResultUtils.error(ErrorCode.PARAMS_ERROR);
        return userBaseResponse;
    }

    /**
     * 注销
     * @param request    USER_LOGIN_STATE
     * @return           注销提示
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        userService.userLogout(request);
        return ResultUtils.success(1);
    }

    /**
     *
     * @param request  前端传入参数：用户id
     * @return         用户信息
     */
    @PostMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        BaseResponse<User> userBaseResponse = user != null ? ResultUtils.success(user) : ResultUtils.error(ErrorCode.NOT_LOGIN);
        return userBaseResponse;
    }

    /**
     * 用户列表搜索
     * @param userName   用户名称
     * @param request    User对象
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String userName, HttpServletRequest request) {
        if (!this.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("username", userName == null ? "" : userName);
        List<User> userList = userService.list(query);
        List<User> safeUserList = userList.stream().map(user -> {
            return userService.getSafeUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(safeUserList);
    }

    /**
     * 删除用户
     * @param id   用户id
     * @param request 判断当前登录用户是否为管理员
     * @return 删除提示
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0 || !this.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request  用户信息
     * @return         是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        return user != null && user.getRole() == USER_ADMIN;
    }


}
