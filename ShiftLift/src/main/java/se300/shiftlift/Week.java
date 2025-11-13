package se300.shiftlift;

import java.util.List;



public class Week {

    private Date week_start_date;
    private Date week_end_date;
    private List<Day> week;

    public Week(Date start_date, Date end_date) {
        if (start_date == null || end_date == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (start_date.get_Date() > end_date.get_Date()) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        this.week_start_date = start_date;
        this.week_end_date = end_date;
    }
    
    public Date getWeekStartDate() {
        return week_start_date;
    }
    
    public Date getWeekEndDate() {
        return week_end_date;
    }

    public List<Day> getWeek() {
        return week;
    }

    
    
}
