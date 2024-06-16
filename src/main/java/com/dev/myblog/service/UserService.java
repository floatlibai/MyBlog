package com.dev.myblog.service;

import com.dev.myblog.pojo.User;

public interface UserService {
    User checkUser(String username, String password);
}
