package com.apex.jwtserver.service.impl;


import com.apex.jwtserver.entity.User;
import com.apex.jwtserver.mapper.UserMapper;
import com.apex.jwtserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByUserName(String userName) {
        return null;
    }
}
