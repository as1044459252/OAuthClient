package com.circles.bookstore.mapper;

import com.circles.bookstore.bean.*;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper {
    List<Book> getAllBooks();
    List<Book> searchBooks(String words);
    Book getBookById(int id);
    void addBook(String bookName,String writer,String description,String imgUrl,double price);
    void deleteBook(int id);
    void updateBook(int id,String bookName,String writer,String description,String imgUrl,double price,double score);
    List<Book> getOnePageBooksById(int bookNumOnePage,int currentPage);
    List<Book> getOnePageBooksByPrice(int bookNumOnePage,int currentPage);

    //评分系统
    void addCusBookScore(Integer customerId,Integer bookId,double score);
    List<CusBookScore> getAllCusBookScore();
    List<CusBookScore> getCusBookScoreByCusId(Integer customerId);
    void updateScore(double score,Integer bookId);
    List<CusBookScore> getCusBookScoreByBookId(Integer bookId);
}
