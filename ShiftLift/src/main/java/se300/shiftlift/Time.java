package se300.shiftlift;

public class Time {

    private final int OPENING_TIME = 800;
    private final int CLOSING_TIME = 1700;

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
    
}
