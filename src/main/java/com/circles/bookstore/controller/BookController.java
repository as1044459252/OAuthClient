package com.circles.bookstore.controller;


import com.circles.bookstore.bean.Book;
import com.circles.bookstore.bean.Customer;
import com.circles.bookstore.bean.Order;
import com.circles.bookstore.service.BookService;
import com.circles.bookstore.service.DealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller

public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    DealService dealService;
    //使用了全局变量，可能会有bug？


    private int checked;

    @RequestMapping("/addBook")
    public String uploadImg(String bookName, String writer,
                            String description,
                            @RequestParam("img") MultipartFile img,
                            Double price, Model model) {
        //判断填写是否为空
        if (bookName == null || writer == null || description == null || img == null || price == null) {
            model.addAttribute("msg", "上传失败!属性不能为空");
            return "administor";
        }
        //不为空
        try {
            //图片上传
            String imgUrl = bookService.exeUpload(img);
            bookService.addBook(bookName, writer, description, imgUrl, price);
            model.addAttribute("msg", "上传成功");
        } catch (Exception e) {
            model.addAttribute("msg", "上传失败，请重试！");
        } finally {
            return "administor";
        }
    }

    @RequestMapping("/getAllBooks")
    @ResponseBody
    public List<Book> selectAllBooks(){
        return bookService.getAllBooks();
    }

    @RequestMapping("/admin/searchBooks")
    @ResponseBody
    public List<Book> searchBooks(HttpServletRequest request){
          return bookService.searchBooks(request.getParameter("words"));
    }

    @RequestMapping("/admin/deleteBook")
    @ResponseBody
    public void deleteBook(int[] numbers){
        for(int number : numbers){
            bookService.deleteBook(number);
        }
    }

    @RequestMapping("/admin/getCurrentBook")
    @ResponseBody
    public Book getCurrentBook(Integer id){
        //这个checked给下面的update用
        //ystem.out.println(id);
        checked = id;
        return bookService.getCurrentBook(id);
    }

    @RequestMapping("/admin/exeUpdateBook")
    public String updateBook(@RequestParam("updateBookName") String updateBookName,
                             String updateWriter,
                             String updateDescription,
                             @RequestParam(required = false) MultipartFile updateImg,
                             Double updatePrice,
                             Double updateScore,
                             Model model){
        //return bookService.getCurrentBook(id);
        if (updateBookName == null ||
                updateWriter == null ||
                updateDescription == null ||
                updatePrice == null ||
                updateScore == null) {
            model.addAttribute("msg", "修改失败!属性不能为空");
        }

        else if(checked<1) {
            model.addAttribute("msg", "请不要刷新页面重复执行!");
            return "administor";
        }
        else {
            Book currentBook = getCurrentBook(checked);
            String imgUrl = bookService.exeUpload(updateImg);
            //如果有修改图片，就上传图片，如果图片为空，就上传当前的url
            if (imgUrl == null) {
                bookService.updateBook(currentBook.getBookId(),updateBookName, updateWriter, updateDescription, currentBook.getImgUrl(), updatePrice, updateScore);
            } else {
                bookService.updateBook(currentBook.getBookId(),updateBookName, updateWriter, updateDescription, imgUrl, updatePrice, updateScore);
            }
            model.addAttribute("msg", "修改成功！");
        }

        return "administor";
    }

    @RequestMapping(value="/getPage{page}")
    @ResponseBody
    public List<Book> getOnePageBooksById(@PathVariable String page){
        List<Book> books = bookService.getOnePageBooksById(Integer.parseInt(page));
        return books;
    }

    @RequestMapping(value="/searchBooks")
    @ResponseBody
    public List<Book> searchBooks(String words){
        List<Book> books = bookService.searchBooks(words);
        return books;
    }

    //获取待评价书籍，代码放在Service中更好？
    @RequestMapping(value="/getMyBooksToBeScored")
    @ResponseBody
    public List<Book> getMyBooksToBeScored(HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginUser");
        Integer customerId = customer.getUid();
        List<Integer> existBookIdList = bookService.getOneCusBookIdsScored(customerId);
        List<Book> resultList = new ArrayList<>();
        List<Order> cusAllOrders = dealService.getCusOrders(customerId);
        List<Integer> cusAllBookIds = new ArrayList<>();
        for(Order cusOrder:cusAllOrders){
            List<Integer> orderBooks = cusOrder.getCart().getBooks();
            cusAllBookIds.addAll(orderBooks);
        }
        //A中去除已经存在的（即已经评分的书籍）
        cusAllBookIds.removeAll(existBookIdList);
        //去重
        cusAllBookIds = cusAllBookIds.stream().distinct().collect(Collectors.toList());
        for(Integer bookId : cusAllBookIds){
            resultList.add(bookService.getCurrentBook(bookId));
        }

        return resultList;
    }

    @RequestMapping(value="/updateScore")
    @ResponseBody
    public String updateScore(Integer bookId,Double score,HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginUser");
        Integer customerId = customer.getUid();
        bookService.addScore(customerId,bookId,score);
        bookService.updateScore(bookId);
        return "suc";
    }

    @RequestMapping(value="/toProducts")
    public String toProducts(Model model){
        List<Book> books = bookService.getSomeBooks();
        model.addAttribute("books",books);
        return "products";
    }

}

