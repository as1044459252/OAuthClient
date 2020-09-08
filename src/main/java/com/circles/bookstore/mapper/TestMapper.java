package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.TestUser;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TestMapper {
    List<TestUser> testSelectAll();

    TestUser testSelectOne(int id);

    void insert(TestUser user);


}
