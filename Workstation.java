package se300.shiftlift;

public class Workstation {


    private String workstation;
    private int numberofEmployees;

    //Default Constructor
    public Workstation() {
    }

    public Workstation(String workstation) {
        if(workstation != null || !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
    }

    //Constructor with name and number of employees
    public Workstation(String workstation, int numberofEmployees) {
        if (workstation != null || !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
        setNumberofEmployees(numberofEmployees);
    }

    public String getName() {
        return workstation;
    }

    public void setName(String workstation) {
        if (workstation != null || !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
    }

    public int getNumberofEmployees() {
        return numberofEmployees;
    }

    public void setNumberofEmployees(int numberOfEmployees) {
        if (numberOfEmployees > 10) {
            throw new IllegalArgumentException("Number of employees cannot be greater than 10.");
        }
        this.numberofEmployees = numberOfEmployees;
    }

    @Override
    public String toString() {
    return "Workstation{name='" + workstation + "', employees=" + numberofEmployees + "}";
    }


}
