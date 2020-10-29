package com.circles.bookstore.config;

import com.circles.bookstore.service.ShiroService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    ShiroService shiroService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("执行授权");
        return null;
    }

    //认证,获取传过来的token，然后根据token获得qq，最后返回info
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userToken = (String)token.getPrincipal();
        String qqNumber = shiroService.getQQByToken(userToken);
        System.out.println(userToken+qqNumber);
        if(qqNumber==null)
            return null;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(qqNumber,userToken,this.getName());
        return info;
    }
}
