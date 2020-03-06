package com.example.employefy.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Employe implements Parcelable
{
    private int id;
    private String email,profileimage,firstname,lastname,contactno,address,city,state,country,status;

    public Employe() {
    }

    public Employe(int id, String email,String profileimage,
                   String firstname, String lastname,
                   String contactno, String address,
                   String city, String state, String country,
                   String status) {
        this.id = id;
        this.email = email;
        this.profileimage=profileimage;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactno = contactno;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
