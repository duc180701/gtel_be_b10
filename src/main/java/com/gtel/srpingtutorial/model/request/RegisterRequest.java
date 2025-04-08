package com.gtel.srpingtutorial.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phoneNumber;

    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
