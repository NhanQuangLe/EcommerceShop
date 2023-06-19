package com.example.ecommerceshop.nhan.Model;

import java.util.ArrayList;

public class Customer{
    String CustomerID, Avatar, Name, PhoneNumber, Email, DateOfBirth, Gender;
    ArrayList<Shop> Followers;

    public Customer() {
    }

    public Customer(String customerID, String avatar, String name, String phoneNumber, String email, String dateOfBirth, String gender, ArrayList<Shop> followers) {
        CustomerID = customerID;
        Avatar = avatar;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        DateOfBirth = dateOfBirth;
        Gender = gender;
        Followers = followers;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public ArrayList<Shop> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<Shop> followers) {
        Followers = followers;
    }
}
