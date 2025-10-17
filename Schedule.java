package com.example.application;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Schedule {
    
    private Date schedule_start_date;
    private Date schedule_end_date;
    private Boolean is_approved;
    
    private ArrayList<Shift> shifts;

    public Schedule()
    {
        shifts = new ArrayList<Shift>();
    }
}
