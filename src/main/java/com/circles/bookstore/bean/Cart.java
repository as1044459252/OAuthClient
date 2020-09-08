package com.circles.bookstore.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {
    private Integer customerId;
    private List<Integer> books;
    private HashMap<Integer,Integer> bookAndNumMap;
    private double totalPrice;
    private static Integer tempCartCustomerId = 0;


    public Cart() {
    }

    //自动生成map
    public Cart(Integer customerId, List<Integer> books) {
        this.customerId = customerId;
        this.books = books;
        HashMap<Integer,Integer> map = new HashMap<>();
        for(Integer book :books){
            map.put(book,1);
        }
        this.bookAndNumMap = map;
    }

    public Cart(Integer customerId, List<Integer> books, HashMap<Integer, Integer> bookAndNumMap, double totalPrice) {
        this.customerId = customerId;
        this.books = books;
        this.bookAndNumMap = bookAndNumMap;
        this.totalPrice = totalPrice;
    }

    //临时Cart
    public Cart(List<Integer> books) {
        this.customerId = tempCartCustomerId;
        this.books = books;
        HashMap<Integer,Integer> map = new HashMap<>();
        for(Integer book :books){
            map.put(book,1);
        }
        this.bookAndNumMap = map;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "customerId='" + customerId + '\'' +
                ", books=" + books +
                ", bookAndNumMap=" + bookAndNumMap +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public List<Integer> getBooks() {
        return books;
    }

    public void setBooks(List<Integer> books) {
        this.books = books;
    }

    public HashMap<Integer, Integer> getBookAndNumMap() {
        return bookAndNumMap;
    }

    public void setBookAndNumMap(HashMap<Integer, Integer> bookAndNumMap) {
        this.bookAndNumMap = bookAndNumMap;
    }
}
