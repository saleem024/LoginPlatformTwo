package com.techkshetrainfo.cltenotes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savsoft-3 on 23/12/16.
 */

public class PaymentResponse {

    @SerializedName("payment")
    @Expose
    private List<Payment> payment = null;

    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

}