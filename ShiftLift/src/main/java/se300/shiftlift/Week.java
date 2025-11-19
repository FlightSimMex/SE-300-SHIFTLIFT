package se300.shiftlift;

import java.util.ArrayList;
import java.util.List;



public class Week {

    private Date week_start_date;
    private Date week_end_date;
    private List<Day> week;
    private List<Shift> shifts;

    public Week(Date start_date, Date end_date) {
        if (start_date == null || end_date == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (start_date.get_Date() > end_date.get_Date()) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        this.week_start_date = start_date;
        this.week_end_date = end_date;
        this.shifts = new ArrayList<>();
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

    public List<Shift> getShifts() {
        return shifts;
    }

    public void addShift(Shift shift) {
        if (shift != null && isShiftInWeek(shift)) {
            shifts.add(shift);
        }
    }

    public boolean isShiftInWeek(Shift shift) {
        if (shift == null || shift.getDate() == null) {
            return false;
        }
        int shiftDate = shift.getDate().get_Date();
        int startDate = week_start_date.get_Date();
        int endDate = week_end_date.get_Date();
        return shiftDate >= startDate && shiftDate <= endDate;
    }

    /**
     * Returns a formatted string representation of the week range.
     */
    public String getWeekRangeString() {
        return week_start_date.toString() + " - " + week_end_date.toString();
    }

    @Override
    public String toString() {
        return "Week{" +
                "start=" + week_start_date +
                ", end=" + week_end_date +
                ", shifts=" + (shifts != null ? shifts.size() : 0) +
                '}';
    }
}
