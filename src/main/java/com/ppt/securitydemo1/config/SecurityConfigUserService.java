package com.ppt.securitydemo1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
    @Autowired
    public DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //需要使用的userDetailService
        auth.userDetailsService(userDetailsService).passwordEncoder(passwd());
    }
    @Bean
    PasswordEncoder passwd(){
        return new BCryptPasswordEncoder();
    }

    //通过这个方法配置操作数据库的对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * @description: 配置一个自定义的登录页面
     * @param: * @param: null
     * @return:
     * @author yzp
     * @date: 2020/11/27 8:55
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置退出设置
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/test/index");

        http.exceptionHandling().accessDeniedPage("/unauth.html");

        http.formLogin() //自定义登录页面
                .loginPage("/login.html") //页面地址
                .loginProcessingUrl("/user/login")  //表单提交的controller层路径
                .defaultSuccessUrl("/success.html").permitAll()   //登录成功的跳转路径,要放行
                //设置哪些路径不需要认证直接可以访问
                .and().authorizeRequests().antMatchers("/","/test/get","/user/login").permitAll()
//                .anyRequest().authenticated() //除了上述請求都要经过认证
  //              .antMatchers("/test/index").hasAuthority("admin") //只针对某一个权限的控制访问
                .antMatchers("/test/index").hasRole("sale1")//role: ROLE_sale
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())//配置操作数据库的对象
                .tokenValiditySeconds(60)//配置token的过期时间
                .userDetailsService(userDetailsService)
                .and()
                .csrf().disable();//关闭跨域


    }
}
