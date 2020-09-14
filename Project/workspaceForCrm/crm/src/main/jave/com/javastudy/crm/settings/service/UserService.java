package com.javastudy.crm.settings.service;

import com.javastudy.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws Exception;

    List<User> getUserList();
}
