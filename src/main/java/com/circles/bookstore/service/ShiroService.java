package com.circles.bookstore.service;

import com.circles.bookstore.bean.Administor;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.mapper.LoginMapper;
import com.circles.bookstore.mapper.ShiroMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShiroService {
    @Autowired
    ShiroMapper shiroMapper;

    public String createToken(){
        String token = RandomStringUtils.randomAlphanumeric(32);
        return token;
    }


    public void saveToken(String qqNumber,String token){
        String oldToken = shiroMapper.findTokenByQQ(qqNumber);
        if(oldToken==null){
            shiroMapper.saveToken(qqNumber,token);
        }
         else{
             shiroMapper.updateToken(qqNumber, token);
        }
    }

    public String getQQByToken(String token){
        return shiroMapper.findQQByToken(token);
    }

    public String findAuthorityByQQ(String qqNumber){
        return shiroMapper.findAuthorityByQQ(qqNumber);
    }


}
