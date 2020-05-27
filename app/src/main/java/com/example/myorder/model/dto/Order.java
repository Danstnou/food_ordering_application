package com.example.myorder.model.dto;

import com.example.myorder.model.entities.ProductCart;
import com.google.firebase.Timestamp;

import java.util.List;
import java.util.UUID;

public class Order {
    public String id;
    public Timestamp date;
    public UserDto user;
    public List<ProductCart> productCartList;
    public int total_cost;
    public String type_payment;
    public String comment;
    public Courier courier;
    public String id_courier;

    public Order() {
    }

    public Order(Timestamp date, UserDto user, List<ProductCart> productCartList,
                 int total_cost, String type_payment, String comment) {
        id = UUID.randomUUID().toString();
        this.date = date;
        this.user = user;
        this.productCartList = productCartList;
        this.total_cost = total_cost;
        this.type_payment = type_payment;
        this.comment = comment;
    }
}