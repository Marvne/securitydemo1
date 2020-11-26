# SpringSecurity 原理
SpringSecurity本质是一个过滤器链
存在很多过滤器,只有每个过滤器都执行放行操作才能执行下一个操作.
##介绍几个简单的过滤器
###FilterSecurityInterceptor
FilterSecurityInterceptor是一个方法级权限过滤器(控制哪些方法能够访问),位于的是整个过滤器链的最底部(最后执行)

表示查看之前的过滤器有没有放行  
``InterceptorStatusToken token = super.beforeInvocation(fi);``

表示真正一个调用过滤服务  
 ``fi.getChain().doFilter(fi.getRequest(), fi.getResponse());``
###ExceptionTranslationFilter
ExceptionTranslationFilter是一个异常过滤器,处理认证过程中抛出的异常逻辑
###UsernamePasswordAuthenticationFilter
UsernamePasswordAuthenticationFilter是一个验证表单登录的过滤器,对/login的post请求做验证,校验表单用户名和密码的合法性
##SpringSecurity中的过滤器是如何加载的(不使用springboot的话)
1.使用Spring boot配置一个DelegatingFilterProxy过滤器

```
Filter delegate = (Filter)wac.getBean(targetBeanName, Filter.class)
targertBeanName:FilterChainProxy;
List<Filter> filters = getFilters(fwRequest);得到所有需要的过滤器
```

##两个重要的接口(UserDetailService和PasswordEncoder)
用于自定义开发,匹配数据库中的账号密码
###UserDetailService
UserDetailService写查询数据库中用户名密码的一个过程  
1.创建一个类继承UsernamePasswordAuthenticationFilter,重写里面的三个方法attemptAuthentication,successfulAuthentication,unsuccessfulAuthentication  
2.创建类实现UserDetailService接口,编写一个查询数据库的过程,返回得到的user对象,这个user对象由spring security提供.
###PasswordEncoder
数据加密接口,用于对返回的User对象里的密码加密.

##web权限方案
###认证
设置登录的用户名和密码
1.配置文件写
```
spring.security.user.name=ppt
spring.security.user.password=ppt
```
2.配置类
```
public class SecurityConfig extends WebSecurityConfigurerAdapter
auth.inMemoryAuthentication().withUser("ppt").password(ppt).roles("admin");
```
3.实现UserDetailService接口
自定义实现类设置

(1).创建配置类,设置使用哪一个UserDetailService的实现类(自己编写,注入到配置类中)   
(2).编写实现类,实现查询逻辑,返回一个给定的User对象,该对象包含了所需的用户名和密码和操作权限  
```
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

    @Service("userDetailsService")
    public class myUserDetailService implements UserDetailsService {
        /**
         * @description: 根据用户名进行的操作
         * @param: * @param: null
         * @return:  User对象
         * @author yzp
         * @date: 2020/11/26 14:37
         */
        //方法的返回类型是接口类型,实际上返回的是该接口的实现类
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            //设置一个权限集合,Collection也是一个接口
            List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
            return new User("ppt",new BCryptPasswordEncoder().encode("ppt"),auths);
        }
    }
```
###授权  
##查询数据库完成认证  
整合mybatisplus完成数据库操作
1.引入相关依赖  
2.创建数据库,表  
3.创建users表所对应的实体类  
4.整合mybatisplus,创建一个接口,继承BaseMapper  
5.在MyUserDetailService中调用mapper里的方法查询数据库来完成用户的认证  
```
QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        //传入一个条件构造器查询对象
        Users users = userMapper.selectOne(queryWrapper);

        if(users == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        //设置一个权限集合,Collection也是一个接口
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
        //从查询的对象中取出相应的用户名和密码
        return new User(users.getUsername(),new BCryptPasswordEncoder().encode(users.getPassword()),auths);
```
6.在启动类上添加注解("MapperScan")


