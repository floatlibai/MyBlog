package com.dev.myblog.service;

import com.dev.myblog.dao.UserRepository;
import com.dev.myblog.po.User;
import com.dev.myblog.util.MD5_utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService_impl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5_utils.code(password)); // add md5 encryption
        return user;
    }
}
