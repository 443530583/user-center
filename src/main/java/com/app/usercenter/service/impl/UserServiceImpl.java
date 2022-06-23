package com.app.usercenter.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.app.usercenter.common.ErrorCode;
import com.app.usercenter.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.app.usercenter.mode.domain.User;
import com.app.usercenter.service.UserService;
import com.app.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;

import static com.app.usercenter.constant.UserConstant.Min;
import static com.app.usercenter.constant.UserConstant.USER_LOGIN_STATE;
import static com.app.usercenter.utils.PasswordDecoderUtil.getMD5Password;

/**
 * @author admin
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2022-06-04 17:10:32
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public long userRegister(String userAccount, String checkPassword, String userPassword,String planetCode) {
        //1.校验前端参数是否符合规则
        boolean flag = registerCheckBody(userAccount, checkPassword, userPassword,planetCode);
        if (flag) {
            //2.对代码进行加密
            String decodePassword = getMD5Password(userPassword);
            //3.插入用户数据
            User user = new User();
            user.setUsername(userAccount);
            user.setCreatetime(new Date());
            user.setUserpassword(decodePassword);
            user.setPlanetCode(planetCode);
            boolean result = this.save(user);
            log.debug("注册成功:" + JSONArray.toJSONString(user));
            return result ? user.getId() : -1;
        } else {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.对代码进行加密
        String decodePassword = getMD5Password(userPassword);
        //2.校验前端参数是否符合规则
        User user = loginCheckBody(userAccount, decodePassword, userPassword);
        //3.用户脱敏
        User safeUser = getSafeUser(user);
        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        log.debug("【登录IP】:" + request.getLocalAddr() +  "【登录成功】:" + JSONArray.toJSONString(user));
        return user;
    }

    /**
     * 注册
     * 对前端的参数进行校验
     *
     * @param userAccount   账户
     * @param checkPassword 校验密码
     * @param userPassword  密码
     * @return 是否符合规则
     */
    private boolean registerCheckBody(String userAccount, String checkPassword, String userPassword,String planetCode) {
        if (StringUtils.isAllBlank(userAccount, checkPassword, userPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名密码不一致");
        }
        if (userAccount.length() <= Min) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户为空");
        }
        if (userPassword.length() <= Min) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码为空");
        }
        //校验账户是否被注册
        QueryWrapper<User> accountQuery = new QueryWrapper<>();
        accountQuery.eq("username", userAccount);
        User accountUser = this.getOne(accountQuery);
        if(accountUser != null){
            throw new BusinessException(ErrorCode.REGISTER_ERROR,"账户已被注册");
        }
        //校验星球账户是否被注册
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.eq("planetCode", planetCode);
        User planetCodeUser = this.getOne(userQuery);
        if(planetCodeUser!=null){
            throw new BusinessException(ErrorCode.REGISTER_ERROR,"星球账户已被注册");
        }
        return true;
    }

    /**
     * 登录
     * 对前端的参数进行校验
     *
     * @param userAccount    账户
     * @param decodePassword 密码密文
     * @return 用户信息
     */
    private User loginCheckBody(String userAccount, String decodePassword, String userPassword) {
        if (StringUtils.isAllBlank(userAccount, decodePassword, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数为空");
        }
        if (userAccount.length() <= Min) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");
        }
        if (userPassword.length() <= Min) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", userAccount);
        User user = userMapper.selectOne(query);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户未注册");
        }
        if(!user.getUserpassword().equals(decodePassword)){
            log.info("user login failed, userPassword cannot match decodePassword!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码错误");
        }
        return user;
    }

    @Override
    public User getSafeUser(User originUser) {
        if (originUser != null) {
            User safeUser = new User();
            safeUser.setId(originUser.getId());
            safeUser.setUsername(originUser.getUsername());
            safeUser.setAvatarurl(originUser.getAvatarurl());
            safeUser.setGender(originUser.getGender());
            safeUser.setPhone(originUser.getPhone());
            safeUser.setEmail(originUser.getEmail());
            safeUser.setUserstatus(originUser.getUserstatus());
            safeUser.setRole(originUser.getRole());
            safeUser.setCreatetime(originUser.getCreatetime());
            safeUser.setUpdatetime(originUser.getUpdatetime());
            safeUser.setIsDelete(originUser.getIsDelete());
            safeUser.setPlanetCode(originUser.getPlanetCode());
            return safeUser;
        }
        return new User();
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_LOGIN_STATE);
    }
}




