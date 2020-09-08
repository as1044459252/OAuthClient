package com.circles.bookstore.controller;

import com.alibaba.fastjson.JSON;
import com.circles.bookstore.bean.Administor;
import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.Cart;
import com.circles.bookstore.bean.Customer;


import com.circles.bookstore.mapper.LoginMapper;
import com.circles.bookstore.service.BookService;
import com.circles.bookstore.service.DealService;
import com.circles.bookstore.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/*
    控制类
    控制登录时的各种页面跳转和业务逻辑
    代码可以更加优化，可以将处理customer的代码（如账号密码检查可以变成一个两个参数的函数）转移到service中，降低耦合

 */

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;
    @Autowired
    DealService dealService;
    @Autowired
    BookService bookService;

    //LoginService loginService = new LoginService();
    //顾客登录成功/失败控制器
    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(required = false) String isAdmin,
                        Model model,
                        HttpSession session
    ){
        //如果是管理员
        if(isAdmin!=null){
            List<Administor> admins = loginService.getAllAdmin();
            for(Administor admin : admins){
                if(admin.getUsername().equals(username)&&admin.getPassword().equals(password)){
                    session.setAttribute("loginAdmin",admin);
                    return "administor";
                }
            }
            model.addAttribute("err_msg","用户名或者密码错误");
            return "login";
        }

        //如果不是管理员，省略了else，是否有代码风格问题？
        List<Customer> users =loginService.getAllCustomers();
        for(Customer user:users){
            //登录成功,既要在session中添加user，同时判断本账号有无购物车，如果没有则新建，最后添加进session中。
            if(user.getUsername().equals(username)&&user.getPassword().equals(password)) {
                List<Integer> allId = dealService.getAllId(); 
                    if (allId.contains(user.getUid())) {
                        Cart cart = dealService.getCartById(user.getUid());
                        session.setAttribute("currentCart", cart);
                    } else {
                        List<Integer> books = new ArrayList<>();
                        Cart newCart = new Cart(user.getUid(), books);
                        newCart.setCustomerId(user.getUid());
                        dealService.addCart(newCart.getCustomerId(), newCart.getBooks(), newCart.getBookAndNumMap());
                        session.setAttribute("currentCart", newCart);
                    }
                    session.setAttribute("loginUser", user);     //在session中添加用户属性loginUser，这样就可以在session生命周期中获得customer的对象
                    return "index";


            }

        }
        model.addAttribute("err_msg","用户名或者密码错误");
        return "login";
    }

    //注册成功/失败控制器，其中输入密码是否相同最好使用js判断。
    @RequestMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("ack_password") String ack_password,
                           Model model){
        System.out.println(password+ack_password);
        List<Customer> users =loginService.getAllCustomers();
        if(!password.equals(ack_password)) {
            model.addAttribute("reg_err_msg","两次输入的密码不匹配，请重试");
            return "register";
        }

        //用户名密码不符合规范判断，暂未实现，可以使用表达式
        /*if(){
            model.addAttribute("reg_err_msg","用户名和密码不符合规范，请重试");
        }*/

        for(Customer user:users){
            if(user.getUsername().equals(username)) {
                model.addAttribute("reg_err_msg", "用户名已存在，请重试");

                return "register";
            }
        }loginService.addCustomer(username,password);
        model.addAttribute("reg_suc_msg","注册成功，现在登录吧");
        return "login";
    }


    /*
    已解决:
    //回到注册链接实现，因为thymeleaf无法完成href直接的跳转，等待其他更简便办法
    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    //同上，跳转到登录页面
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    //同上，跳转到个人信息页面
    @RequestMapping("/toCusInfo")
    public String toCusInfo(){
        return "cusInfo";
    }
    */

    //登出控制器，返回登录页面
    @RequestMapping("/toLogout")
    public String toLogout(HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginUser");
        Administor admin = (Administor)session.getAttribute("loginAdmin");
        if(admin!=null)
            session.removeAttribute("loginAdmin");
        if(customer!=null){
            session.removeAttribute("loginUser");
            session.removeAttribute("currentCart");
        }
        return "login";


    }

    @RequestMapping("/toCusInfo")
    public String toCusInfo(HttpSession session,Model model) {
        Customer customer = (Customer) session.getAttribute("loginUser");
        if (customer != null) {
            return "cusInfo";
        }
        else {
            model.addAttribute("err_msg","请在登录后使用此功能！");
            return "login";
        }
    }





}
