package se300.shiftlift;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;


@Entity
@Table(name = "workstations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DescriminatorColumn(name = "dtype")
public class Workstation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "workstation_id")
    private Long id;

    @Column(name = "workstation_name")
    private String workstation;

    @Embedded
    private Time operation_hours;


    

    //Default Constructor
    public Workstation() {

    }

    public Workstation(String workstation) {
        if(workstation != null && !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return workstation;
    }

    public void setName(String workstation) {
        if (workstation != null && !workstation.isEmpty()) {
            this.workstation = workstation;
        } else {
            throw new IllegalArgumentException("Workstation name cannot be null or empty");
        }
    }

    

    public Time getOperation_hours() {
        return operation_hours;
    }

    public void station_opening(int opening_time) {
        if (this.operation_hours == null) {
            this.operation_hours = new Time(); // Initialize with default hours
        }
        this.operation_hours.set_start_time(opening_time);
    }

    public void station_closing(int closing_time) {
        if (this.operation_hours == null) {
            this.operation_hours = new Time(); // Initialize with default hours
        }
        this.operation_hours.set_end_time(closing_time);
    }
    
    public void setOperation_hours(Time operation_hours) {
        this.operation_hours = operation_hours;
    }



    @Override
    public String toString() {
    return "Workstation{name='" + workstation + "'}";
    }


}
