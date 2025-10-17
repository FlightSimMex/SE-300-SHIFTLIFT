package com.example.application;

public class Workstation {


    private String workstation;

    public Workstation(String workstation) {
        if(workstation != null || !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
    }

    public String getName() {
        return workstation;
    }
}
