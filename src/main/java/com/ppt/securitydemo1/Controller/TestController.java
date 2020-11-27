package com.ppt.securitydemo1.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yzp
 * @version 1.0
 * @description: TODO
 * @date 2020/11/26 10:04
 */
@RestController
@RequestMapping("/test")
public class TestController {


    //默认存在登录认证的页面 (user:***)
    @GetMapping("get")
    public String get(){
        return "hello security";
    }

    //登录成功的首页
    @GetMapping("index")
    public String index(){
        return "hello index";
    }

    @GetMapping("update")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String update(){
        return  "hello update";
    }



}
