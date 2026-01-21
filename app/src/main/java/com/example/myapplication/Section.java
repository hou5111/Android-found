package com.example.myapplication;

import java.util.List;

public class Section {
    private String letter; // 字母（A/B/C/#）
    private List<Contact> contacts; // 该字母下的联系人

    public Section(String letter, List<Contact> contacts) {
        this.letter = letter;
        this.contacts = contacts;
    }

    // Getter
    public String getLetter() { return letter; }
    public List<Contact> getContacts() { return contacts; }
}
