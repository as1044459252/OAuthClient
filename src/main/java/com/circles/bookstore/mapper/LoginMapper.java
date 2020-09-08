package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.Administor;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LoginMapper {
    List<Customer> getAllCustomers();
    Customer getCustomerById(int id);
    Integer getCusByUsername(String username); //第一次查询使用username，而后使用id，提高速度?
    void addCustomer(@Param("username") String username, @Param("password")String pwd, String state);
    void updateCusInfo(@Param("tel") String tel, @Param("address")String address,Integer id);

    //管理员
    List<Administor> getAllAdmin();

}
