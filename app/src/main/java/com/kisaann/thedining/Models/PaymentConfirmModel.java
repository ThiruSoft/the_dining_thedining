package com.kisaann.thedining.Models;

import java.util.List;

public class PaymentConfirmModel {
    private String phone;
    private String name;
    private String address;
    private String customerNo;
    private String status;
    private String comments;
    private String paymentMethod;
    private String total;
    private String promoDiscount;
    private String walletDiscount;
    private String paymentState;
    private String restaurantId;
    private String restaurantOwner;
    private String orderId;
    private String latLng;
    private String tableNo;
    private String time;
    private String date;
    private String razorPayPaymentId;
    private String month;
    private String year;
    private String totalQnty;
    private String restName;
    private String restAddress;
    private String gstNo;
    private String paymentOtp;
    private String tipAmount;
    private String foodGst;
    private String serviceTax;
    private String offerType;
    private String offerDiscount;
    private String nc;
    private String day;
    private String timeLine;
    private String orderFrom;
    private String kot;
    private String ncType;
    private String cardAmount;
    private String cashAmount;
    private String upiAmount;
    private String dineOutAmount;
    private String nearBuyAmount;
    private String magicPinAmount;
    private String littleAppAmount;
    private String holdAmount;
    private String paymentMethods;
    private String serviceCharge;
    private String discountType;
    private String rateValue;
    private String rateComments;
    private List<Order> food; // list of food order

    public PaymentConfirmModel() {
    }

    public PaymentConfirmModel(String phone, String name, String address, String customerNo, String status, String comments, String paymentMethod, String total, String promoDiscount, String walletDiscount, String paymentState, String restaurantId, String restaurantOwner, String orderId, String latLng, String tableNo, String time, String date, String razorPayPaymentId, String month, String year, String totalQnty, String restName, String restAddress, String gstNo, String paymentOtp, String tipAmount, String foodGst, String serviceTax, String offerType, String offerDiscount, String nc, String day, String timeLine, String orderFrom, String kot, String ncType, String cardAmount, String cashAmount, String upiAmount, String dineOutAmount, String nearBuyAmount, String magicPinAmount, String littleAppAmount, String holdAmount, String paymentMethods, String serviceCharge, String discountType, String rateValue, String rateComments, List<Order> food) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.customerNo = customerNo;
        this.status = status;
        this.comments = comments;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.promoDiscount = promoDiscount;
        this.walletDiscount = walletDiscount;
        this.paymentState = paymentState;
        this.restaurantId = restaurantId;
        this.restaurantOwner = restaurantOwner;
        this.orderId = orderId;
        this.latLng = latLng;
        this.tableNo = tableNo;
        this.time = time;
        this.date = date;
        this.razorPayPaymentId = razorPayPaymentId;
        this.month = month;
        this.year = year;
        this.totalQnty = totalQnty;
        this.restName = restName;
        this.restAddress = restAddress;
        this.gstNo = gstNo;
        this.paymentOtp = paymentOtp;
        this.tipAmount = tipAmount;
        this.foodGst = foodGst;
        this.serviceTax = serviceTax;
        this.offerType = offerType;
        this.offerDiscount = offerDiscount;
        this.nc = nc;
        this.day = day;
        this.timeLine = timeLine;
        this.orderFrom = orderFrom;
        this.kot = kot;
        this.ncType = ncType;
        this.cardAmount = cardAmount;
        this.cashAmount = cashAmount;
        this.upiAmount = upiAmount;
        this.dineOutAmount = dineOutAmount;
        this.nearBuyAmount = nearBuyAmount;
        this.magicPinAmount = magicPinAmount;
        this.littleAppAmount = littleAppAmount;
        this.holdAmount = holdAmount;
        this.paymentMethods = paymentMethods;
        this.serviceCharge = serviceCharge;
        this.discountType = discountType;
        this.rateValue = rateValue;
        this.rateComments = rateComments;
        this.food = food;
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

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
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

    public String getRazorPayPaymentId() {
        return razorPayPaymentId;
    }

    public void setRazorPayPaymentId(String razorPayPaymentId) {
        this.razorPayPaymentId = razorPayPaymentId;
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

    public List<Order> getFood() {
        return food;
    }

    public void setFood(List<Order> food) {
        this.food = food;
    }

    public String getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(String promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public String getWalletDiscount() {
        return walletDiscount;
    }

    public void setWalletDiscount(String walletDiscount) {
        this.walletDiscount = walletDiscount;
    }

    public String getTotalQnty() {
        return totalQnty;
    }

    public void setTotalQnty(String totalQnty) {
        this.totalQnty = totalQnty;
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

    public String getPaymentOtp() {
        return paymentOtp;
    }

    public void setPaymentOtp(String paymentOtp) {
        this.paymentOtp = paymentOtp;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getFoodGst() {
        return foodGst;
    }

    public void setFoodGst(String foodGst) {
        this.foodGst = foodGst;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(String offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(String timeLine) {
        this.timeLine = timeLine;
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

    public String getNcType() {
        return ncType;
    }

    public void setNcType(String ncType) {
        this.ncType = ncType;
    }

    public String getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getUpiAmount() {
        return upiAmount;
    }

    public void setUpiAmount(String upiAmount) {
        this.upiAmount = upiAmount;
    }

    public String getDineOutAmount() {
        return dineOutAmount;
    }

    public void setDineOutAmount(String dineOutAmount) {
        this.dineOutAmount = dineOutAmount;
    }

    public String getNearBuyAmount() {
        return nearBuyAmount;
    }

    public void setNearBuyAmount(String nearBuyAmount) {
        this.nearBuyAmount = nearBuyAmount;
    }

    public String getMagicPinAmount() {
        return magicPinAmount;
    }

    public void setMagicPinAmount(String magicPinAmount) {
        this.magicPinAmount = magicPinAmount;
    }

    public String getLittleAppAmount() {
        return littleAppAmount;
    }

    public void setLittleAppAmount(String littleAppAmount) {
        this.littleAppAmount = littleAppAmount;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(String holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getRateComments() {
        return rateComments;
    }

    public void setRateComments(String rateComments) {
        this.rateComments = rateComments;
    }
}
