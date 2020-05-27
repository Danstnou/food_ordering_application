package com.example.myorder.model.entities;

import com.example.myorder.model.dto.UserDto;

import java.util.Map;

public class User {
    String name;
    String phone;
    String address;

    Map<String, Boolean> roles;

    public User() {
    }

    public User(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public User(UserDto userDTO, Map<String, Boolean> roles) {
        this.name = userDTO.getName();
        this.phone = userDTO.getPhone();
        this.address = userDTO.getAddress();
        this.roles = roles;
    }

    public void setFieldsDto(UserDto userDTO) {
        this.name = userDTO.getName();
        this.phone = userDTO.getPhone();
        this.address = userDTO.getAddress();
    }
    /*
     * Геттеры для доступа Firebase
     */

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, Boolean> getRoles() {
        return roles;
    }

    /*
     * Сеттеры
     */

    public void setName(String name) {
        this.name = name;
    }
}