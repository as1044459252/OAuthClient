package com.circles.bookstore.controller;

import com.alibaba.fastjson.JSON;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CusController {
    @Autowired
    LoginMapper loginMapper;

    @RequestMapping("/updateInfo")
    public String  update(String tel, String address, Model model, HttpSession session){
        if(tel==null||address==null){
            model.addAttribute("err_msg","字段不能为空！");
        }
        else {
            Customer customer = (Customer)session.getAttribute("loginUser");
            loginMapper.updateCusInfo(tel, address,customer.getUid());
            model.addAttribute("err_msg","修改成功！");
        }
        return "cusInfo";
    }

    @RequestMapping("/getAllUsers")
    @ResponseBody
    public String getAllUsers(){
        List<Customer> users = loginMapper.getAllCustomers();
        String userJson = JSON.toJSONString(users);
        return userJson;
    }



}
