package com.ppt.securitydemo1.Controller;

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

    @GetMapping("get")
    public String get(){
        return "hello security";
    }

}
