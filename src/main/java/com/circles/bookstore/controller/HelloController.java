package com.circles.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*@Controller
public class oauthLoginController {

    @RequestMapping("/index")
    public String sayHello(){
        return "index";
    }
}*/



@org.springframework.stereotype.Controller

public class HelloController {
    @RequestMapping({"/","index"})
    public String toIndex(){
        return "index";
    }
}

