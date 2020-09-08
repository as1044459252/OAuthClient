package com.circles.bookstore.bean;

public class CusBookScore {
    Integer customerId;
    Integer bookId;
    double score;

    public CusBookScore() {
    }

    public CusBookScore(Integer customerId, Integer bookId, double score) {
        this.customerId = customerId;
        this.bookId = bookId;
        this.score = score;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "cusBookScore{" +
                "customerId=" + customerId +
                ", bookId=" + bookId +
                ", score=" + score +
                '}';
    }
}
