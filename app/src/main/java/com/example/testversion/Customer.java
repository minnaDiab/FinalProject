package com.example.testversion;

public class Customer {
    protected String name, email, address,phone;
    protected String userPic;


    public Customer(String name, String email, String address, String phone, String userPic) {

        this.name = name;
        this.email = email;
        this.address = address;
        this.phone=phone;
        this.userPic = userPic;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }


}
