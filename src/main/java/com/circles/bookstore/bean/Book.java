package com.circles.bookstore.bean;

public class Book {
    private Integer bookId;
    private String name;
    private String writer;
    private String description;
    private String imgUrl;
    private double price;
    private double score; //评分

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Book() {
    }

    public Book(Integer bookId, String name, String writer, String description, String imgUrl, double price, double score) {
        this.bookId = bookId;
        this.name = name;
        this.writer = writer;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.score = score;
    }


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", name='" + name + '\'' +
                ", writer='" + writer + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
