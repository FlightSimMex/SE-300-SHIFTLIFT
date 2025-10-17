package com.example.application;

import java.util.ArrayList;

public class TestingCenterWorkstations {
    
    private ArrayList<Workstation> workstations;


    public TestingCenterWorkstations() {
        this.workstations = new ArrayList<>();
    }

    public void addWorkstation(Workstation workstation) {
        this.workstations.add(workstation);
    }

    public Workstation removeWorkstation(String workstation_name)
    {
        for (Workstation workstation : this.workstations) {
            if (workstation.getName().equals(workstation_name)) {
                workstations.remove(workstation);
                return workstation;
            }
        }
        return null; // Return null if no matching workstation is found
    }
}
