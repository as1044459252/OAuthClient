package com.circles.bookstore.service;

import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.CusBookScore;
import com.circles.bookstore.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/*
    书籍业务
 */
@Service
public class BookService {
    //本地服务器存放图片文件夹路径
    String serverPath = "E:\\fakeServer\\book";
    static int bookNumOnePage = 12;
    @Autowired
    BookMapper bookMapper;
    //执行上传文件的业务 返回资源路径 在yml中配置过
    public String exeUpload(MultipartFile img){
        try {
            // 原始图片名称
            String oldFileName = img.getOriginalFilename(); // 获取上传文件的原名
            // 新的图片名称
            String newFileName = UUID.randomUUID() + oldFileName.substring(oldFileName.lastIndexOf("."));
            // 新图片
            File newFile = new File(serverPath + "\\" + newFileName);
            // 将内存中的数据写入磁盘
            img.transferTo(newFile);
            return newFileName;
        }
        catch (Exception e){
            return null;
        }
    }

    //数据库操作上传书籍信息
    public void addBook(String bookName,String writer,String description,String imgUrl,double price){
        bookMapper.addBook(bookName,writer,description,imgUrl,price);
    }

    //数据库操作查看所有
    public List<Book> getAllBooks(){
        List<Book> books = bookMapper.getAllBooks();
        return bookMapper.getAllBooks();
    }

    //数据库操作搜索
    public List<Book> searchBooks(String words){
        return bookMapper.searchBooks(words);
    }

    //数据库操作删除
    public void deleteBook(int id){
        bookMapper.deleteBook(id);
    }

    //数据库操作删除
    public Book getCurrentBook(int id){
        return bookMapper.getBookById(id);
    }

    //数据库操作上传
    public void updateBook(int id,
                           String updateBookName,
                           String updateWriter,
                           String updateDescription,
                           String updateImg,
                           Double updatePrice,
                           Double updateScore)
    {
        bookMapper.updateBook(id,updateBookName,updateWriter,updateDescription,updateImg,updatePrice,updateScore);
    }

    public List<Book> getOnePageBooksById(int currentPage){
        return bookMapper.getOnePageBooksById(bookNumOnePage,(currentPage-1)*bookNumOnePage);
    }

    public List<Book> getOnePageBooksByPrice(int currentPage){
        return bookMapper.getOnePageBooksByPrice(bookNumOnePage,currentPage);
    }

    public List<CusBookScore> getAllCusBookScore(){
        return bookMapper.getAllCusBookScore();
    }

    public List<Book> getOneCusBooksScored(Integer cusId){
        List<CusBookScore> cusBookScores = bookMapper.getCusBookScoreByCusId(cusId);
        List<Book> resultBooks = new ArrayList<>();
        for (CusBookScore cbs : cusBookScores){
            Book book = bookMapper.getBookById(cbs.getBookId());
            resultBooks.add(book);
        }
        return resultBooks;
    }

    public List<Integer> getOneCusBookIdsScored(Integer cusId){
        List<CusBookScore> cusBookScores = bookMapper.getCusBookScoreByCusId(cusId);
        List<Integer> resultBooks = new ArrayList<>();
        for (CusBookScore cbs : cusBookScores){
            resultBooks.add(cbs.getBookId());
        }
        return resultBooks;
    }

    public void updateScore(Integer bookId){
        List<CusBookScore> cusBookScores = bookMapper.getCusBookScoreByBookId(bookId);
        double score;
        double sum=0;
        int i=0;
        for(i=0;i<cusBookScores.size();i++){
            sum+=cusBookScores.get(i).getScore();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        score = Double.parseDouble(df.format(sum/i));
        bookMapper.updateScore(score,bookId);
    }

    public void addScore(Integer cusId,Integer bookId, double score){
        bookMapper.addCusBookScore(cusId,bookId,score);
    }

    public List<Book> getSomeBooks(){
        Random random = new Random();
        int bookId1 = random.nextInt(19);
        int bookId2 = random.nextInt(19);
        int bookId3 = random.nextInt(19);
        List<Book> books = new ArrayList<>();
        books.add(bookMapper.getBookById(bookId1));
        books.add(bookMapper.getBookById(bookId2));
        books.add(bookMapper.getBookById(bookId3));
        return books;
    }
}
