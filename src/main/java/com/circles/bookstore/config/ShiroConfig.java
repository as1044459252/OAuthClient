package com.circles.bookstore.config;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        // 还可以设置其他的filter，如Map<String,String> filterMap = new LinkedHashMap<>();
        Map<String, Filter> authFilter = new HashMap<>();
        //将自定义的Filter加入
        authFilter.put("auth",new AuthFilter());
        bean.setFilters(authFilter);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/checkLogin", "auth");
        //filterMap.put("/checkAdmin", "auth");
        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }

    //参数的含义是 使用被springboot接管的下面定义的userRealm
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联Realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建realm对象并使用,下面是将其交给springboot
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

}
