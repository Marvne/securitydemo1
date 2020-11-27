package com.ppt.securitydemo1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ppt.securitydemo1.entity.Users;
import org.springframework.stereotype.Repository;

/**
 * @description: mapper接口
 * @author yzp
 * @date 2020/11/26 15:20
 * @version 1.0
 * 泛型里写该接口所对应的实体类
 * 加上Repository注解是为了在该接口没有自己写实现类的情况下使用autowire不报错
 */
@Repository
public interface UserMapper extends BaseMapper<Users> {

}
