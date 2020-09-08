package com.circles.bookstore.controller;


import com.alibaba.fastjson.JSON;
import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.Cart;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.bean.Order;
import com.circles.bookstore.service.BookService;
import com.circles.bookstore.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//因为book采用了Integer的模式，导致本控制类代表较复杂，基本是将list和map里的变量从Integer转成Book的过程。
public class DealController {
    @Autowired
    BookService bookService;
    @Autowired
    DealService dealService;
    //使用了全局变量，可能会有bug？

    private int checked;

    @RequestMapping(value="/toSingle{id}")
    public String getSingleBook(@PathVariable String id, Model model){
        Book book = bookService.getCurrentBook(Integer.parseInt(id));
        model.addAttribute("currentBook",book);
        return "single";
    }

    @RequestMapping(value="/toCheckOne{id}")
    //为了html页面的统一，这里也返回一个map
    public String toCheckOne(@PathVariable String id, Model model, HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginUser");
        if(customer!=null) {
            Book book = bookService.getCurrentBook(Integer.parseInt(id));
            HashMap<Book, Integer> bookAndNumMap = new HashMap<>();
            bookAndNumMap.put(book, 1);
            List<Integer> books = new ArrayList<>();
            books.add(book.getBookId());
            Cart cart = new Cart(books);
            model.addAttribute("bookAndNumMap", bookAndNumMap);
            model.addAttribute("totalPrice", book.getPrice());
            model.addAttribute("cartType", 0);
            session.setAttribute("tempCart", cart);
            return "checkout";
        }
        else {
            model.addAttribute("err_msg","请先登录后再使用此功能！");
            return "login";
        }
    }

    @RequestMapping(value="/addBookToCart{bookId}")
    @ResponseBody
    public String addBookToCart(HttpSession session, Model model, @PathVariable Integer bookId){
        Customer customer = (Customer)session.getAttribute("loginUser");
        if(customer!=null) {
            Cart cart = (Cart) session.getAttribute("currentCart");
            Book book = bookService.getCurrentBook(bookId);
            if (cart != null && customer != null) {
                List<Integer> oldBooks = cart.getBooks();
                oldBooks.add(bookId);
                Cart newCart = new Cart(customer.getUid(), oldBooks);
                dealService.addOneBookToCart(newCart.getCustomerId(), book);
                //更新的过程在service中实现，因此这里不能直接返回newCart，应该重新取cart
                Cart finCart = dealService.getCartById(customer.getUid());
                //所有的购物车操作都要改变session
                session.setAttribute("currentCart", finCart);
                return JSON.toJSONString(finCart);
            } else
                return null;
        }
        else {
            model.addAttribute("err_msg","请先登录后再使用此功能！");
            return null;
        }
    }

    @RequestMapping(value="/toCheckCart")
    public String toCheckCart(HttpSession session,Model model){
        Customer customer = (Customer)session.getAttribute("loginUser");
        if(customer!=null) {
            Cart currentCart = (Cart) session.getAttribute("currentCart");
            List<Integer> books = currentCart.getBooks();
            HashMap<Book, Integer> bookAndNumMap = new HashMap<>();
            HashMap<Integer, Integer> bookIdAndNumMap = currentCart.getBookAndNumMap();
            for (Integer bookId : books) {
                Book nowBook = bookService.getCurrentBook(bookId);
                bookAndNumMap.put(nowBook, bookIdAndNumMap.get(bookId));
            }
            model.addAttribute("bookAndNumMap", bookAndNumMap);
            model.addAttribute("totalPrice", currentCart.getTotalPrice());
            model.addAttribute("cartType", 1);
            return "checkout";
        }
        else {
            model.addAttribute("err_msg","请先登录后再使用此功能！");
            return "login";
        }
    }

    //到订单显示页面，判断是否是临时购物车
    @RequestMapping(value="/toOrder{cartType}")
    public String toOrder(HttpSession session, Model model, @PathVariable String cartType){
        Customer customer = (Customer)session.getAttribute("loginUser");
        if(Integer.parseInt(cartType)==1) {
            Cart cart = (Cart) session.getAttribute("currentCart");
            if (cart != null) {
                Order order = dealService.addAndGetOrder(cart,customer.getAddress());
                model.addAttribute("currentOrder",order);
                dealService.emptyCart(cart.getCustomerId());
                Cart emptyCart = dealService.getCartById(cart.getCustomerId());
                session.setAttribute("currentCart",emptyCart);
                return "order";
            }
            else {
                model.addAttribute("err_msg","会话超时！请重新登录");
                return "login";
            }
        }
        else {
            Cart cart = (Cart) session.getAttribute("tempCart");
            if (cart != null) {
                Order order = dealService.addAndGetOrder(cart,customer.getAddress());
                model.addAttribute("currentOrder",order);
                session.removeAttribute("tempCart");
                return "order";
            }
            else {
                model.addAttribute("err_msg","会话超时！请重新登录");
                return "login";
            }
        }
    }

    @RequestMapping(value="/getMyOrders")
    @ResponseBody
    public String getMyOrders(HttpSession session,Model model){
        Customer customer = (Customer) session.getAttribute("loginUser");
        List<Order> orders = dealService.getCusOrders(customer.getUid());
        String orderJson = JSON.toJSONString(orders);
        return orderJson;
    }

    @RequestMapping(value="/getAllOrders")
    @ResponseBody
    public String getAllOrders(HttpSession session,Model model){
        List<Order> orders = dealService.getAllOrders();
        String orderJson = JSON.toJSONString(orders);
        return orderJson;
    }

    @RequestMapping(value="/bookNumPlus")
    @ResponseBody
    public String bookNumPlus(HttpSession session,Integer bookId){
        Cart cart = (Cart)session.getAttribute("currentCart");
        double bookPrice = bookService.getCurrentBook(bookId).getPrice();
        Customer customer = (Customer)session.getAttribute("loginUser");
        HashMap<Integer,Integer> oldMap = cart.getBookAndNumMap();
        Integer oldBookNum = oldMap.get(bookId);
        oldMap.replace(bookId,++oldBookNum);
        cart.setBookAndNumMap(oldMap);
        cart.setTotalPrice(cart.getTotalPrice()+bookPrice);
        dealService.updateCart(customer.getUid(),cart.getBooks(),oldMap,cart.getTotalPrice());
        String cartJson = JSON.toJSONString(cart);
        session.setAttribute("currentCart",cart);
        return cartJson;
    }

    @RequestMapping(value="/bookNumMinus")
    @ResponseBody
    public String bookNumMinus(HttpSession session,Integer bookId){
        Cart cart = (Cart)session.getAttribute("currentCart");
        double bookPrice = bookService.getCurrentBook(bookId).getPrice();
        Customer customer = (Customer)session.getAttribute("loginUser");
        HashMap<Integer,Integer> oldMap = cart.getBookAndNumMap();
        Integer oldBookNum = oldMap.get(bookId);
        oldMap.replace(bookId,--oldBookNum);
        cart.setBookAndNumMap(oldMap);
        cart.setTotalPrice(cart.getTotalPrice()-bookPrice);
        dealService.updateCart(customer.getUid(),cart.getBooks(),oldMap,cart.getTotalPrice());
        String cartJson = JSON.toJSONString(cart);
        session.setAttribute("currentCart",cart);
        return cartJson;
    }

}

