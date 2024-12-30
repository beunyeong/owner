package com.example.oner.enums;


public enum MemberRole {

    WORKSPACE("workspace"),
    BOARD("board"),
    READ("read");

    private final String role;

    MemberRole(String role){ this.role = role;}

    public String getRole(){ return role; }

}
