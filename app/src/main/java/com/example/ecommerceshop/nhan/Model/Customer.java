package com.example.ecommerceshop.nhan.Model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
    String Avatar, Name, PhoneNumber, Email, DateOfBirth, Gender;
    ArrayList<Shop> Followers;

    public Customer() {
    }

    public Customer(String userID, String userType, String avatar, String name, String phoneNumber, String email, String dateOfBirth, String gender, ArrayList<Shop> followers) {
        super(userID, userType);
        Avatar = avatar;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        DateOfBirth = dateOfBirth;
        Gender = gender;
        Followers = followers;
    }

    public Customer(String avatar, String name, String phoneNumber, String email, String dateOfBirth, String gender, ArrayList<Shop> followers) {
        Avatar = avatar;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        DateOfBirth = dateOfBirth;
        Gender = gender;
        Followers = followers;
    }

    public ArrayList<Shop> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<Shop> followers) {
        Followers = followers;
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
}
