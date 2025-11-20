package se300.shiftlift;

import jakarta.persistence.Embeddable;

@Embeddable
public class Time {

    public static final int OPENING_TIME = 800;
    public static final int CLOSING_TIME = 1700;

    private int start_time;
    private int end_time;

    public Time(int start_time, int end_time) {
        
        if(start_time < OPENING_TIME || end_time > CLOSING_TIME || start_time >= end_time) {
            throw new IllegalArgumentException("Shift times outside operating hours (0800-1700)");

        }else{
            this.start_time = start_time;
            this.end_time = end_time;
        }
    }

    public Time() {
        this.start_time = OPENING_TIME;
        this.end_time = CLOSING_TIME;
    }

    public int getStart_time() {
        return start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void set_start_time(int start_time) {
        if(start_time_is_valid(start_time)) {
            this.start_time = start_time;
        } else {
            throw new IllegalArgumentException("Start time outside operating hours (0800-1700)");
        }
    }

    public void set_end_time(int end_time) {
        if(end_time_is_valid(end_time)) {
            this.end_time = end_time;
        } else {
            throw new IllegalArgumentException("End time outside operating hours (0800-1700)");
        }
    }

    private boolean start_time_is_valid(int start_time) {
        return start_time >= OPENING_TIME && start_time < CLOSING_TIME;
    }
    private boolean end_time_is_valid(int end_time) {
        return end_time <= CLOSING_TIME && end_time > OPENING_TIME;
    }
    
    @Override
    public String toString() {
        return formatTime(start_time) + " - " + formatTime(end_time);
    }
    
    private String formatTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        String period = hours < 12 ? "AM" : "PM";
        
        // Convert to 12-hour format
        int displayHours = hours;
        if (hours == 0) {
            displayHours = 12;
        } else if (hours > 12) {
            displayHours = hours - 12;
        }
        
        return String.format("%d:%02d %s", displayHours, minutes, period);
    }
    
    /**
     * Calculate the duration of this shift in hours
     * @return the duration in hours (as a double)
     */
    public double getDurationInHours() {
        int startHours = start_time / 100;
        int startMinutes = start_time % 100;
        int endHours = end_time / 100;
        int endMinutes = end_time % 100;
        
        int totalStartMinutes = startHours * 60 + startMinutes;
        int totalEndMinutes = endHours * 60 + endMinutes;
        
        return (totalEndMinutes - totalStartMinutes) / 60.0;
    }
    
}
