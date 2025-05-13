package com.example.fakeneptun.model;

public class Student {
    private String userName;
    private String email;
    private String familyName;
    private String firstName;

    public Student() { }

    public Student(String userName, String email, String familyName, String firstName) {
        this.userName = userName;
        this.email = email;
        this.familyName = familyName;
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
