package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CartMapper {
    List<Cart> getAllCarts();
    Cart getCartById(int id);
    void addCart(Integer customerId, String books, String bookAndNumMaps);
    void updateCart(Integer customerId, String books, String bookAndNumMaps, double price);
    List<Integer> getAllId();
    String getBooksById(int id);
    String getMapById(int id);
    double getPriceById(int id);

}
