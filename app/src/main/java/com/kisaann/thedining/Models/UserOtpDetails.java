package com.kisaann.thedining.Models;

public class UserOtpDetails {

    private String phoneNo;
    private String cashPayment;
    private String otpNo;
    private String transactionNo;
    private String orderId;
    private String orderStatus;

    public UserOtpDetails() {
    }

    public UserOtpDetails(String phoneNo, String cashPayment, String otpNo, String transactionNo, String orderId, String orderStatus) {
        this.phoneNo = phoneNo;
        this.cashPayment = cashPayment;
        this.otpNo = otpNo;
        this.transactionNo = transactionNo;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(String cashPayment) {
        this.cashPayment = cashPayment;
    }

    public String getOtpNo() {
        return otpNo;
    }

    public void setOtpNo(String otpNo) {
        this.otpNo = otpNo;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
