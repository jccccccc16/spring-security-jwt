package com.cjc.config;

import com.cjc.config.component.CustomUrlDecisionManager;
import com.cjc.config.component.JwtAuthencationTokenFilter;
import com.cjc.config.component.RestfulAccessDeniedHandler;
import com.cjc.pojo.Admin;
import com.cjc.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/13
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AdminService adminService;

    @Resource
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 开放登录url
        http.authorizeRequests().anyRequest()
                .authenticated()
                .antMatchers("/login").permitAll()
                .antMatchers("/login.html").permitAll()
                .antMatchers("/js/**").permitAll();

        // 使用jwt，不需要csrf
        http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .headers()
                .cacheControl();
        // 添加验证过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .and()
                .formLogin()
                .passwordParameter("password")
                .usernameParameter("username")
                .loginProcessingUrl("/login")
                .loginPage("/login.html");



    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }


}
