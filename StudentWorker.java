package com.example.application;



public class StudentWorker extends User{
    private int max_hours = 20;
    private int scheduled_hours;
    //Shifts assigned to student worker

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
        return "StudentWorker{" + "username='" + getUsername() + '\'' + ", email='" + getEmail() + '\'' + ", scheduled_hours=" + scheduled_hours + '}';
    }
    


}
