package com.kisaann.thedining.Models;

public class CustomerSupportModel {
    private String name;
    private String email;
    private String subject;
    private String message;
    private String time;
    private String date;
    private String timeDate;
    private String type;

    public CustomerSupportModel() {
    }

    public CustomerSupportModel(String name, String email, String subject, String message, String time, String date, String timeDate, String type) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.time = time;
        this.date = date;
        this.timeDate = timeDate;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
