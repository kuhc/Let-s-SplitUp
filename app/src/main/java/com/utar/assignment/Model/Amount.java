package com.utar.assignment.Model;

import java.util.Date;

public class Amount {
    private String Id;
    private String payerId;
    private String OwnerId;
    private double amount;
    private Date createdDate;

    public Amount() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public double getAmount() {
        return amount;
    }

    public void updateAmount(double temp_amount) {
        this.amount = amount + temp_amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
