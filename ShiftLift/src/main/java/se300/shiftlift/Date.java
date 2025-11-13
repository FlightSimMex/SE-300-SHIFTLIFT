package se300.shiftlift;

import jakarta.persistence.Embeddable;

@Embeddable
public class Date {
    @SuppressWarnings("FieldMayBeFinal")
    private int day;
    @SuppressWarnings("FieldMayBeFinal")
    private int month;
    @SuppressWarnings("FieldMayBeFinal")
    private int year;
    private boolean open;

    // Default constructor required by JPA
    public Date() {
    }

    //Missing input validation
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.open = true;
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

    public void set_open_status(boolean status)
    {
        this.open = status;
    }

    public boolean get_open_status()
    {
        return open;
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

}
