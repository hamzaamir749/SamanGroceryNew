package com.saman.groceryshopping.HelperClasses;

public class Session {
    String id,name,phone,address,paddress;

    public Session(String id, String name, String phone, String address, String paddress) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.paddress = paddress;
    }

    public String getPaddress() {
        return paddress;
    }

    public String getId() {
        return id;
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
