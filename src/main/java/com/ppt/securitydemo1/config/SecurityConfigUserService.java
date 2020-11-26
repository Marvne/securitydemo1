package com.ppt.securitydemo1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author yzp
 * @version 1.0
 * @description: TODO
 * @date 2020/11/26 14:23
 *
 */
@Configuration
public class SecurityConfigUserService extends WebSecurityConfigurerAdapter {

    //注入自己写的userDeatilService
    @Autowired
    public UserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //需要使用的userDetailService
        auth.userDetailsService(userDetailsService).passwordEncoder(passwd());
    }
    @Bean
    PasswordEncoder passwd(){
        return new BCryptPasswordEncoder();
    }
}
