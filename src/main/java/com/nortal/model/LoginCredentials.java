package com.nortal.model;

public class LoginCredentials {

    public String username;
    public String password;

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}