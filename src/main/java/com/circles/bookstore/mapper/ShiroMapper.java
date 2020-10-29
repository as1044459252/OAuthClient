package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShiroMapper {
    String findTokenByQQ(String qqNumber);
    String findQQByToken(String token);
    String findAuthorityByQQ(String qqNumber);
    void saveToken(String qqNumber,String token);
    void updateToken(String qqNumber,String token);

    String findRoleByQQ(String qqNumber);
}
