package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.Cart;
import com.circles.bookstore.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    List<Order> getAllOrder();
    Order getOrderById(int id);
    void addOrder(String cart,String time,String state,String orderUid,String address);
    List<String> getAllCarts();
    String getCartById(int id);
    //void updateOrders(Integer id,String cart);
}
