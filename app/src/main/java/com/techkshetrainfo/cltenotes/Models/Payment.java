package com.techkshetrainfo.cltenotes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by savsoft-3 on 23/12/16.
 */

public class Payment {

    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("paid_date")
    @Expose
    private String paidDate;
    @SerializedName("payment_gateway")
    @Expose
    private String paymentGateway;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("email")
    @Expose
    private String email;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}