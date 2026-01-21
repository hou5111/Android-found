package com.example.myapplication;

import java.io.Serializable;

public class Contact implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String gender;
    private String note;
    private String firstLetter;

    public Contact(int id, String name, String phone, String gender, String note) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.note = note;
        this.firstLetter = PinyinUtils.getFirstLetter(name);
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.firstLetter = PinyinUtils.getFirstLetter(name);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFirstLetter() {
        return firstLetter;
    }
}