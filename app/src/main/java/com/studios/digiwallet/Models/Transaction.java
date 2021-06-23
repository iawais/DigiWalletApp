package com.studios.digiwallet.Models;

import java.sql.Timestamp;

public class Transaction {
    public String title, purpose, details, senderName, senderNumber, recName, recNumber;
    public float amount, fee;
    public Timestamp timestamp;
    public String direction;

    public Transaction() {
    }

    public Transaction(String title, String purpose, String details, String senderName, String senderNumber, String recName, String recNumber, float amount, float fee, Timestamp timestamp, String direction) {
        this.title = title;
        this.purpose = purpose;
        this.details = details;
        this.senderName = senderName;
        this.senderNumber = senderNumber;
        this.recName = recName;
        this.recNumber = recNumber;
        this.amount = amount;
        this.fee = fee;
        this.timestamp = timestamp;
        this.direction = direction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecNumber() {
        return recNumber;
    }

    public void setRecNumber(String recNumber) {
        this.recNumber = recNumber;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "title='" + title + '\'' +
                ", purpose='" + purpose + '\'' +
                ", details='" + details + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderNumber='" + senderNumber + '\'' +
                ", recName='" + recName + '\'' +
                ", recNumber='" + recNumber + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", timestamp=" + timestamp +
                ", direction='" + direction + '\'' +
                '}';
    }
}
