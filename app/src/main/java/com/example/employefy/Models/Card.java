package com.example.employefy.Models;

public class Card
{
    private String profile,firstname,lastname,contactno,address,city,state,country;

    public Card(String profile, String firstname, String lastname,
                String contactno, String address, String city,
                String state, String country) {
        this.profile = profile;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactno = contactno;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Card() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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
}
