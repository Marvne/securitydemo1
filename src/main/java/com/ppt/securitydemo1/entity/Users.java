package com.ppt.securitydemo1.entity;

import lombok.Data;

/**
 * @author yzp
 * @version 1.0
 * @description: TODO
 * @date 2020/11/26 15:17
 */
@Data
public class Users {
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
