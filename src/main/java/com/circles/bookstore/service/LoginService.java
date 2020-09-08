package com.circles.bookstore.service;

import com.circles.bookstore.bean.Administor;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
    LoginMapper loginMapper;

    public List<Customer> getAllCustomers(){
        return loginMapper.getAllCustomers();
    }

    public Customer getCustomerById(int id){
        return loginMapper.getCustomerById(id);
    }

    public int getCusByUsername(String username){
        return loginMapper.getCusByUsername(username);
    };

    public void addCustomer(String username,String pwd){
        loginMapper.addCustomer(username,pwd,"normal");
        return;
    };

    public  void updateCusInfo(String tel,String address,Integer id){
        loginMapper.updateCusInfo(tel, address,id);
    };

    public List<Administor> getAllAdmin(){return loginMapper.getAllAdmin();};
}
