package com.javastudy.crm.settings.service.impl;

import com.javastudy.crm.exception.LoginException;
import com.javastudy.crm.settings.dao.UserDao;
import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.settings.service.UserService;
import com.javastudy.crm.utils.DateTimeUtil;
import com.javastudy.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException{
        Map<String,String> map = new HashMap<String,String>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userDao.login(map);
        if(user == null){
            throw new LoginException("账号密码错误");
        }
        //验证实现失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState = user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }
        //判断IP是否有效
        String allowIps = user.getAllowIps();
        if(allowIps != null){
            if(!allowIps.contains(ip)){
                throw new LoginException("IP地址受限");
            }
        }
        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }
}
