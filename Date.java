package com.example.application;

public class Date {
    private int day;
    private int month;
    private int year;

    //Missing input validation
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    public int get_day() {
        return day;
    }
    public int get_month() {
        return month;
    }
    public int get_year() {
        return year;
    }

    public int get_Date()
    {
        return day + month*100 + year*10000;
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

}
