package org.synergy.prom.enumuration;

public enum UserType {
    USER("USER"),
    ARTIST("ARTIST");

    private String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }
}