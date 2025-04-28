package com.java.Invista.controller;

import com.java.Invista.entity.UserEntity;
import com.java.Invista.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserEntity create(@RequestBody UserEntity user) {
        return userService.create(user);
    }

}
