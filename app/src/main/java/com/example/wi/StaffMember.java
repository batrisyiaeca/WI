package com.example.wi;

public class StaffMember {
    private String email;
    private String fName;
    private String role;

    // Empty constructor needed for Firestore
    public StaffMember() {}

    public StaffMember(String email, String fName, String role) {
        this.email = email;
        this.fName = fName;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return fName;
    }

    public String getRole() {
        return role;
    }
}
