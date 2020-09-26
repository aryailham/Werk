package com.example.werk.model;

public class Candidates {
    private String name;
    private String email;
    private String linkedIn;
    private int desiredSalary;
    private String pitch;

    public Candidates(String name, String email, String linkedIn, int desiredSalary, String pitch) {
        this.name = name;
        this.email = email;
        this.linkedIn = linkedIn;
        this.desiredSalary = desiredSalary;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public int getDesiredSalary() {
        return desiredSalary;
    }

    public void setDesiredSalary(int desiredSalary) {
        this.desiredSalary = desiredSalary;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }
}
