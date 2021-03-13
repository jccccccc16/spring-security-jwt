package com.cjc.controller;

import com.cjc.jwt.JwtTokenUtil;
import com.cjc.pojo.Admin;
import com.cjc.pojo.RespBean;
import com.cjc.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/13
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 **/
@RestController
public class LoginController {

    @Resource
    private AdminService adminService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostMapping("/login")
    public RespBean login(Admin admin){

        UserDetails userDetails = adminService.loadUserByUsername(admin.getUsername());

        if(admin.getPassword() == null || userDetails.getPassword().equals(admin.getPassword())){
            return RespBean.success("密码或账号错误");
        }

        // 由于我们接管了登录，我们需要把用户信息放到security中，相当于已经登录了
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        // 为该用户生成jwt
        String jwt = jwtTokenUtil.generateToken(userDetails);
        HashMap<String, String> tokenMap = new HashMap<String, String>();

        // 将jwt封装发送到前端
        tokenMap.put("token",jwt);
        tokenMap.put("tokenHead",tokenHead);

        return RespBean.success("登录成功",tokenMap);


    }


}
