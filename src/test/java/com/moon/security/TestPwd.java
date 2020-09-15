package com.moon.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPwd {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }
}
