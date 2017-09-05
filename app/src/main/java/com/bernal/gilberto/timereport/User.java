package com.bernal.gilberto.timereport;

/**
 * Created by Registered User on 9/3/2017.
 */

public class User {
    public String name;
    public String address;
    public String phone;
    public int hour_value;
    public boolean admin;
    public String companyId;


    public User() {
    }

    public User(String name, String address, String phone, int hour_value, boolean admin, String companyId) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.hour_value = hour_value;
        this.admin = admin;
        this.companyId = companyId;

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public int getHour_value() {
        return hour_value;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getCompanyId() {
        return companyId;
    }

}

