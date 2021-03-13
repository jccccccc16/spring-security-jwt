package com.cjc.service;

import com.cjc.pojo.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/13
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 **/
@Component
public class AdminService implements UserDetailsService {

    private final static Map<String,Admin> adminsMapper = new HashMap<String, Admin>();

    static{
        adminsMapper.put("chen01",new Admin(1,"chen01","chen01"));
        adminsMapper.put("chen02",new Admin(2,"chen02","chen02"));
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminsMapper.get(username);
        if(admin!=null){
            return admin;
        }
        throw new UsernameNotFoundException("账号或密码不存在");
    }



}
