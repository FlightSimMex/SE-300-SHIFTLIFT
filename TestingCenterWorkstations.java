package se300.shiftlift;

import java.util.ArrayList;

public class TestingCenterWorkstations {
    
    private ArrayList<Workstation> workstations;


    public TestingCenterWorkstations() {
        this.workstations = new ArrayList<>();
    }

    public void addWorkstation(Workstation workstation) {
        if (workstation !=null){
            this.workstations.add(workstation);
            System.out.println("Workstation added: " + workstation.toString());
        }
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

    //Find workstation by name
    public Workstation findWorkstationByName(String workstation_name) {
        for (Workstation workstation : this.workstations) {
            if (workstation.getName().equals(workstation_name)) {
                return workstation;
            }
        }
        return null; // Return null if no matching workstation is found
    }

    //Edit an existing workstation and number of employees
    public boolean editWorkstation(String workstation_name, String new_name, int new_numberofEmployees) {
        Workstation workstation = findWorkstationByName(workstation_name);
        if (workstation != null) {
            workstation.setName(new_name);
            workstation.setNumberofEmployees(new_numberofEmployees);
            return true; // Edit successful
        }
        return false; // Workstation not found
    }

    //Get all workstations
    public ArrayList<Workstation> getWorkstations() {
        return this.workstations;
    }
}
