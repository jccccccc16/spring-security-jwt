package com.cjc.config.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 自定义用户角色过滤
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/3
 * Time: 11:34
 * To change this template use File | Settings | File Templates.
 **/
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {


    /**
     * 在这个方法中你可以对用户权限进行判断，判断该用户是否有权限访问该链接
     * 由于是demo，我只能简单，知识判断该用户是否有登录
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {


        /**
         * 如果该用户没有进行登录的话，那么该authentication应该是AnonymousAuthenticationToken类型
         */
        if(authentication instanceof AnonymousAuthenticationToken) {

                throw new AccessDeniedException("尚未登录，请登录");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
