package com.example.myorder.model.dto;

public class InputDataOrder {
    String name;
    String phone;
    String address;
    String type_payment;
    String comment;

    public InputDataOrder(String name, String phone, String address, String type_payment, String comment) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type_payment = type_payment;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getType_payment() {
        return type_payment;
    }

    public String getComment() {
        return comment;
    }
}
