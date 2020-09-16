package com.moon.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class UserController {

    @RequestMapping("/all")
    @ResponseBody
    public String  index(){
       return "任何人可以访问!";
    }

    @RequestMapping("/level1")
    @ResponseBody
    public String  level1(){
        return "vip可以访问!";
    }

    @RequestMapping("/level2")
    @ResponseBody
    public String  level2(){
        return "manager可以访问!";
    }

    @RequestMapping("/level3")
    @ResponseBody
    public String  level3(){
        return "admin可以访问!";
    }

    @RequestMapping("/testResource/{id}")
    @ResponseBody
    public String  testResource(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "拿到授权才能访问图片!"+id;
    }

    @RequestMapping("/testDemo/{id}")
    @ResponseBody
    public String  testDemo(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "拿到授权才能访问图片!"+id;
    }
}
