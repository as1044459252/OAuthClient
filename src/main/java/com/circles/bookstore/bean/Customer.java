package com.circles.bookstore.bean;

public class Customer extends User {
    private String tel;
    private String state;
    private String address;

    public Customer(){

    }

    public Customer(Integer uid, String username, String password, String tel, String state, String address) {
        super(uid, username, password);
        this.tel = tel;
        this.state = state;
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //自写的toString
    /*@Override
    public String toString() {
        return "Customer{" +"username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\''+
                ", tel='" + tel + '\'' +
                ", state='" + state + '\'' +
                ", address='" + address + '\'' +
                '}';
    }*/

    //自动生成的toString
    @Override
    public String toString() {
        return "Customer{" +
                "tel='" + tel + '\'' +
                ", state='" + state + '\'' +
                ", address='" + address + '\'' +
                "} " + super.toString();
    }


}
