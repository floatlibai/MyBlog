package com.dev.myblog.service;

import com.dev.myblog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
