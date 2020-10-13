package com.saman.groceryshopping.Models;

public class AddressClass {
    String name,email,address,country,city,postal_code,phone,checkout_type;

    public AddressClass(String name, String email, String address, String country, String city, String postal_code, String phone, String checkout_type) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.country = country;
        this.city = city;
        this.postal_code = postal_code;
        this.phone = phone;
        this.checkout_type = checkout_type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getCheckout_type() {
        return checkout_type;
    }
}
