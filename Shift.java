package com.example.application;

public class Shift {

    private Date assigned_date;
    private StudentWorker assigned_Worker;
    private Time assigned_time;
    private Workstation assigned_workstation;


    public Shift(Date date, Time time, Workstation workstation, StudentWorker worker)
    {
        this.assigned_date = date;
        this.assigned_time = time;
        this.assigned_workstation = workstation;
        this.assigned_Worker = worker;
    }


    public Date getDate() {
        return assigned_date;
    }

    public Time getTime() {
        return assigned_time;
    }

    public StudentWorker getStudentWorker() {
        return assigned_Worker;
    }

    public Workstation getWorkstation() {
        return assigned_workstation;
    }

    public void changeWorkstation(Workstation newWorkstation) {
        this.assigned_workstation = newWorkstation;
    }

    public void changeTime(Time newTime) {
        this.assigned_time = newTime;
    }

    
}
