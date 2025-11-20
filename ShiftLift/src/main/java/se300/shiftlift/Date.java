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
    
    /**
     * Convert this Date to a LocalDate for easier date calculations
     */
    public java.time.LocalDate toLocalDate() {
        return java.time.LocalDate.of(year, month, day);
    }
    
    /**
     * Get the Friday that starts the work week containing this date
     * Work week is defined as Friday-Thursday
     */
    public java.time.LocalDate getWorkWeekStart() {
        java.time.LocalDate date = toLocalDate();
        java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        // If it's Friday, Saturday, Sunday, Monday, Tuesday, Wednesday, or Thursday
        // we need to find the most recent Friday (or current day if it's Friday)
        if (dayOfWeek == java.time.DayOfWeek.FRIDAY) {
            return date;
        } else if (dayOfWeek == java.time.DayOfWeek.SATURDAY || dayOfWeek == java.time.DayOfWeek.SUNDAY) {
            // Go back to the previous Friday
            return date.with(java.time.temporal.TemporalAdjusters.previous(java.time.DayOfWeek.FRIDAY));
        } else {
            // Monday-Thursday, go back to the previous Friday
            return date.with(java.time.temporal.TemporalAdjusters.previous(java.time.DayOfWeek.FRIDAY));
        }
    }
    
    /**
     * Check if this date is in the same work week as another date
     * Work week is Friday-Thursday
     */
    public boolean isSameWorkWeek(Date other) {
        return this.getWorkWeekStart().equals(other.getWorkWeekStart());
    }

}
