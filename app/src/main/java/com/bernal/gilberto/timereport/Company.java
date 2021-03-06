package com.bernal.gilberto.timereport;


public class Company {
    public String id;
    public String name;
    public String address;
    public String phone;
    public String emailAddress;
    public String period;
    public String periodBegin;
    public String status;


    public Company(String id,String name, String address, String phone, String emailAddress, String period, String periodBegin,String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.period = period;
        this.periodBegin = periodBegin;
        this.status = status;
    }

    public String getName() { return name;  }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getStatus() {
        return status;
    }

    public String getPeriod()  {return period;}

    public  String getPeriodBegin() {return periodBegin;}

    public String getId() {
        return id;
    }
}
