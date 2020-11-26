package com.ppt.securitydemo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author yzp
 * @version 1.0
 * @description: 配置类(用户名和密码)
 * @date 2020/11/26 13:53
 * 自定义的security配置类的话需要继承WebSecurityConfigurerAdapter才可以
 */
//@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //对密码进行加密在加密过程中需要使用到PasswordEncoder接口对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String ppt = passwordEncoder.encode("ppt");
        //配置用户名和密码
        auth.inMemoryAuthentication().withUser("ppt").password(ppt).roles("admin");

    }

    //在加密过程中需要使用到PasswordEncoder接口对象,所以将该对象的实现类加入容器
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
