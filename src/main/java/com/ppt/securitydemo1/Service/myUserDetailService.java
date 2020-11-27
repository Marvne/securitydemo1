package com.ppt.securitydemo1.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ppt.securitydemo1.entity.Users;
import com.ppt.securitydemo1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzp
 * @version 1.0
 * @description: 自定义的UserDetailService的实现类,仅仅做的是从数据库里得到用户和密码的一个过程,不做验证
 * @date 2020/11/26 14:36
 */
@Service("userDetailsService")
public class myUserDetailService implements UserDetailsService {
    @Autowired
    private  UserMapper userMapper;

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

        //调用userMapper查询数据库的用户信息
        //QuerWrapper是一个条件构造器,相当于"where username = username"
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        //传入一个条件构造器查询对象
        Users users = userMapper.selectOne(queryWrapper);

        if(users == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        //设置一个权限集合,Collection也是一个接口
        List<GrantedAuthority> auths =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_sale");
        //从查询的对象中取出相应的用户名和密码
        return new User(users.getUsername(),new BCryptPasswordEncoder().encode(users.getPassword()),auths);
    }
}
