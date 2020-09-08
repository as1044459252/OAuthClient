package com.circles.bookstore.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.Cart;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.bean.Order;
import com.circles.bookstore.mapper.BookMapper;
import com.circles.bookstore.mapper.CartMapper;
import com.circles.bookstore.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
    交易业务
 */
@Service
public class DealService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    OrderMapper orderMapper;

    public List<Cart> getAllCarts(){
        return cartMapper.getAllCarts();
    }

    public Cart getCartById(Integer id){
        List<Integer> books = JSON.parseObject(cartMapper.getBooksById(id),List.class);
        HashMap<Integer, Integer> map = JSON.parseObject(cartMapper.getMapById(id),HashMap.class);
        return new Cart(id,books,map,cartMapper.getPriceById(id));
    }

    public void addCart(Integer customerId, List<Integer> books, HashMap<Integer,Integer> bookAndNumMaps){
        String booksStr = JSON.toJSONString(books);
        String maps = JSON.toJSONString(bookAndNumMaps);
        cartMapper.addCart(customerId,booksStr,maps);
    }

    //增加一本书
    public void addOneBookToCart(Integer customerId, Book book){
        Cart cart = getCartById(customerId);
        List<Integer> books = cart.getBooks();
        HashMap<Integer,Integer> bookAndNumMaps = cart.getBookAndNumMap();
        double newPrice = cart.getTotalPrice()+book.getPrice();
        if(books.contains(book.getBookId())){
            Integer oldValue = bookAndNumMaps.get(book.getBookId());
            bookAndNumMaps.replace(book.getBookId(),++oldValue);
            cartMapper.updateCart(customerId,JSON.toJSONString(books),JSON.toJSONString(bookAndNumMaps),newPrice);
        }
        else {
            books.add(book.getBookId());
            bookAndNumMaps.put(book.getBookId(),1);
            cartMapper.updateCart(customerId,JSON.toJSONString(books),JSON.toJSONString(bookAndNumMaps),newPrice);
        }
    }

    public List<Integer> getAllId(){
        return cartMapper.getAllId();
    }

    public Cart jsonToCart(Integer customerId, String books, String bookAndNumMaps, double price){
        List<Integer> newBooks= JSON.parseObject(books,List.class);
        HashMap<Integer,Integer> hashMap= JSON.parseObject(bookAndNumMaps,HashMap.class);
        return new Cart(customerId,newBooks,hashMap,price);
    }

    public List<Order> getAllOrders(){
        List<Order> orders = orderMapper.getAllOrder();
        for(Order order:orders) {
            String cartJson = orderMapper.getCartById(order.getOrderId());
            Cart cart = JSON.parseObject(cartJson,Cart.class);
            order.setCart(cart);
        }
        return orders;
    }


    public Order addAndGetOrder(Cart cart,String address){
        Order order = new Order();
        order.setCart(cart);
        order.setState("created");
        String cartJson = JSON.toJSONString(cart);
        orderMapper.addOrder(cartJson,order.getTime(),order.getState(),order.getOrderUid(),address);
        return order;
    }

    public void emptyCart(Integer customerId) {
        String emptyBooks = "[]";
        String emptyBookAndNumMaps = "{}";
        cartMapper.updateCart(customerId,emptyBooks,emptyBookAndNumMaps,0);
    }

    public List<Order> getCusOrders(Integer cusId){
        List<Order> allOrders= getAllOrders();
        List<Order> resultOrder= new ArrayList<>();
        for(Order order: allOrders){
            if(order.getCart().getCustomerId()==cusId)
                resultOrder.add(order);
        }
        return resultOrder;
    }

    /*public void updateOrder(Integer id,Cart cart){
        String cartJson = JSON.toJSONString(cart);
        orderMapper.updateOrders(id,cartJson);
    }*/
    public void updateCart(Integer id,List<Integer> books,HashMap<Integer,Integer> bookAndNumMap,double price) {
        String booksJson = JSON.toJSONString(books);
        String mapJson = JSON.toJSONString(bookAndNumMap);
        cartMapper.updateCart(id,booksJson,mapJson,price);
    }

}
