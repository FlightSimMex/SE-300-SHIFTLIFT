package se300.shiftlift;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class StudentWorker extends User{
    @Transient
    private int max_hours = 20;
    private int scheduled_hours;
    //Shifts assigned to student worker

    public StudentWorker()
    {
        
    }

    public StudentWorker(String email, String password) {
        super(email, password);
    }

    public int getScheduled_hours() {
        return scheduled_hours;
    }

    public void setScheduled_hours(int hours) {
        if (hours < 0 || hours > max_hours) throw new IllegalArgumentException("scheduled hours out of range");
        this.scheduled_hours = hours;
    }

    @Override
    public String toString() {
        return "StudentWorker{" + "username='" + getUsername()+ '\'' + "initials='" + getInitials() + '\'' + ", email='" + getEmail() + '\'' +  "Passowrd='" + getPassword() + '\'' + ", scheduled_hours=" + scheduled_hours + '}';
    }
    


}
