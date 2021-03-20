package com.kisaann.thedining.Models;

public class VerifyOtpModel {

    private String userPhoneNo;
    private String userName;
    private String tableNo;
    private String otpNo;
    private String time;
    private String date;
    private String status;
    private String accepted;

    public VerifyOtpModel() {
    }

    public VerifyOtpModel(String userPhoneNo, String userName, String tableNo, String otpNo, String time, String date, String status, String accepted) {
        this.userPhoneNo = userPhoneNo;
        this.userName = userName;
        this.tableNo = tableNo;
        this.otpNo = otpNo;
        this.time = time;
        this.date = date;
        this.status = status;
        this.accepted = accepted;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getOtpNo() {
        return otpNo;
    }

    public void setOtpNo(String otpNo) {
        this.otpNo = otpNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }
}
