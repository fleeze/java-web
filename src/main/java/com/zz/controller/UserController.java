package com.zz.controller;

import com.zz.model.User;
import com.zz.response.HttpStatus;
import com.zz.response.ResponseObject;
import com.zz.repository.UserRepository;
import com.zz.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Map<String, Object>> addNewUser (@RequestBody User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return ResponseObject.error(HttpStatus.CONFLICT.getCode(), "用户名已经存在");
        }
        User user1 = userRepository.save(user);
        Map<String, Object> map = new HashMap<>(2);
        String token = JwtUtil.generateToken(user.getUserId());
        map.put("token", token);
        map.put("userId", user1.getUserId());
        map.put("userName", user.getUserName());
        return ResponseObject.success(map);
    }

    @PostMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Map<String, Object>> login(@RequestBody User user) {
        if (userRepository.existsByUserNameAndPassword(user.getUserName(), user.getPassword())) {
            User user1 = userRepository.findByUserName(user.getUserName());
            Map<String, Object> map = new HashMap<>(2);
            map.put("userId", user1.getUserId());
            map.put("userName", user.getUserName());
            String token = JwtUtil.generateToken(user1.getUserId());
            map.put("token", token);
            return ResponseObject.success(map);
        }
        return ResponseObject.error(HttpStatus.UNAUTHORIZED.getCode(), "用户名或密码错误");
    }

    @GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObject<Iterable<User>> getAllUsers(@RequestHeader(name = "Authorization", required = false) String token) {
        if (JwtUtil.validateToken(token)) {
            return ResponseObject.error(HttpStatus.UNAUTHORIZED.getCode(), "token无效");
        }
        Iterable<User> users = userRepository.findAll();
        return ResponseObject.success(users);
    }

}