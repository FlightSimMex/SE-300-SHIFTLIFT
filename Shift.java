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
}
