package com.circles.bookstore.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Order {
    private Integer orderId;
    private String orderUid;
    private Cart cart;
    private String time;
    private String state;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Order() {
        //设置时间
        SimpleDateFormat sFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        //获取系统当前时间并将其转换为string类型
        String time=sFormat.format(calendar.getTime());
        this.time = time;

        //设置Uid
        this.orderUid=UUID.randomUUID().toString();
    }



    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderUid='" + orderUid + '\'' +
                ", cart=" + cart +
                ", time='" + time + '\'' +
                ", state='" + state + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getOrderUid() {
        return orderUid;
    }

    public void setOrderUid(String orderUid) {
        this.orderUid = orderUid;
    }
}
