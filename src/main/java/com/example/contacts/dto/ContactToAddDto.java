package com.example.contacts.dto;

public class ContactToAddDto {
    public String firstName;
    public String lastName;
    public int age;

    public ContactToAddDto(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
