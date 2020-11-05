package com.circles.bookstore.config;

import com.circles.bookstore.service.ShiroService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    ShiroService shiroService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String qqNumber = principals.toString();
        String role = shiroService.findAuthorityByQQ(qqNumber);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(role);
        System.out.println("执行授权");
        return simpleAuthorizationInfo;
    }

    //认证,获取传过来的token，然后根据token获得qq，最后返回info
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userToken = (String)token.getPrincipal();
        String qqNumber = shiroService.getQQByToken(userToken);
        System.out.println("执行认证"+userToken+qqNumber);
        if(qqNumber==null)
            return null;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(qqNumber,userToken,this.getName());
        return info;
    }
}
