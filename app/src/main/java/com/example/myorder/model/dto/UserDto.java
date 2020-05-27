package com.example.myorder.model.dto;

public class UserDto {
    String name;
    String phone;
    String address;

    public UserDto() {
    }

    public UserDto(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
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
}
