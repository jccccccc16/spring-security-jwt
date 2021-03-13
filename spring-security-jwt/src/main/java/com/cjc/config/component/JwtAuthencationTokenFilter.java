package com.cjc.config.component;

import com.cjc.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/2/28
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 * 当用户访问时，该过滤器会对请求头的jwt进行判断是否存在jwt，如果存在，是否存在该用户信息；
 * 若不存在该用户信息，那么将该用户放入到securityContext中
 **/
@Slf4j
public class JwtAuthencationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;




    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        log.info("进入：JwtAuthencationTokenFilter");

        String header = httpServletRequest.getHeader(tokenHeader);

        // 获取header
        log.info("获取header: "+header);

        // 是否存在token
        if(null !=header && header.startsWith(tokenHead)){

            String jwt = header.substring(tokenHead.length());

            // 获取jwt
            log.info("获取jwt： "+jwt);

            String username = jwtTokenUtil.getUserNameFromToken(jwt);

            // 获取username
            log.info("获取username："+username);

            // token存在但是没有登录
            if(null != username && null == SecurityContextHolder.getContext().getAuthentication()){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 验证token是否有效
                if(jwtTokenUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }

        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
