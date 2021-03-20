package com.kisaann.thedining.Models;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String address;
    private String status;
    private String comments;
    private String paymentMethod;
    private String total;
    private String paymentState;
    private String restaurantId;
    private String restaurantOwner;
    private String orderId;
    private String latLng;
    private String tableNo;
    private String time;
    private String date;
    private String month;
    private String year;
    private String restName;
    private String restAddress;
    private String gstNo;
    private String print;
    private String printFood;
    private String orderFrom;
    private String kot;
    private String printPizza;
    private String cashPaid;
    private String cashOtp;
    private String notification;
    private List<Order> food; // list of food order

    public Request() {
    }

    public Request(String phone, String name, String address, String status, String comments, String paymentMethod, String total, String paymentState, String restaurantId, String restaurantOwner, String orderId, String latLng, String tableNo, String time, String date, String month, String year, String restName, String restAddress, String gstNo, String print, String printFood, String orderFrom, String kot, String printPizza, String cashPaid, String cashOtp, String notification, List<Order> food) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = status;
        this.comments = comments;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.paymentState = paymentState;
        this.restaurantId = restaurantId;
        this.restaurantOwner = restaurantOwner;
        this.orderId = orderId;
        this.latLng = latLng;
        this.tableNo = tableNo;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.restName = restName;
        this.restAddress = restAddress;
        this.gstNo = gstNo;
        this.print = print;
        this.printFood = printFood;
        this.orderFrom = orderFrom;
        this.kot = kot;
        this.printPizza = printPizza;
        this.cashPaid = cashPaid;
        this.cashOtp = cashOtp;
        this.notification = notification;
        this.food = food;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantOwner() {
        return restaurantOwner;
    }

    public void setRestaurantOwner(String restaurantOwner) {
        this.restaurantOwner = restaurantOwner;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestAddress() {
        return restAddress;
    }

    public void setRestAddress(String restAddress) {
        this.restAddress = restAddress;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getPrintFood() {
        return printFood;
    }

    public void setPrintFood(String printFood) {
        this.printFood = printFood;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public String getKot() {
        return kot;
    }

    public void setKot(String kot) {
        this.kot = kot;
    }

    public String getPrintPizza() {
        return printPizza;
    }

    public void setPrintPizza(String printPizza) {
        this.printPizza = printPizza;
    }

    public String getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(String cashPaid) {
        this.cashPaid = cashPaid;
    }

    public String getCashOtp() {
        return cashOtp;
    }

    public void setCashOtp(String cashOtp) {
        this.cashOtp = cashOtp;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public List<Order> getFood() {
        return food;
    }

    public void setFood(List<Order> food) {
        this.food = food;
    }
}
