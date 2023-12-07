package com.example.example100.user.model;

public enum UserStatus {
    NONE, USING, STOP;
    int value;

    UserStatus() {

    }

    public int getValue() {
        return this.value;
    }
}
