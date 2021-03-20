package com.kisaann.thedining.Models;

public class BookTableModel {
    private String bookId;
    private String name;
    private String phone;
    private String bookDate;
    private String bookTime;
    private String people;
    private String restaurantName;
    private String restaurantPhone;
    private String time;
    private String date;
    private String month;
    private String year;
    private String status;
    private String tableNo;
    private String source;
    private String restaurantId;

    public BookTableModel() {
    }

    public BookTableModel(String bookId, String name, String phone, String bookDate, String bookTime, String people, String restaurantName, String restaurantPhone, String time, String date, String month, String year, String status, String tableNo, String source, String restaurantId) {
        this.bookId = bookId;
        this.name = name;
        this.phone = phone;
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.people = people;
        this.restaurantName = restaurantName;
        this.restaurantPhone = restaurantPhone;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.status = status;
        this.tableNo = tableNo;
        this.source = source;
        this.restaurantId = restaurantId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
