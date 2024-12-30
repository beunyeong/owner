package com.example.oner.enums;

public enum MemberWait {
    WAIT("wait"),
    ACTIVE("active");

    private String value;

    MemberWait(String value) {this.value = value;}

    public String getValue() { return value;}
}
