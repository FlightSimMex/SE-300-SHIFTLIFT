package com.example.application;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    
    private Date schedule_start_date;
    private Date schedule_end_date;
    private Boolean is_approved;
    
    private ArrayList<Shift> shifts;

    public Schedule(Date start, Date end)
    {
        shifts = new ArrayList<Shift>();
        this.schedule_start_date = start;
        this.schedule_end_date = end;
        this.is_approved = false;
    }

    public Date getSchedule_start_date() {
        return schedule_start_date;
    }

    public Date getSchedule_end_date() {
        return schedule_end_date;
    }

    public Boolean getIs_approved() {
        return is_approved;
    }

    public void approve_schedule() {
        this.is_approved = true;
    }

    public Shift addShift(Date date, Time time, Workstation workstation, StudentWorker studentWorker) {
        Shift new_shift = new Shift(date, time, workstation, studentWorker);
        shifts.add(new_shift);
        return new_shift;
    }

    public Shift findShift(Date date, int startTime, StudentWorker studentWorker)
    {
        for (Shift shift : shifts) {
            if (shift.getDate().equals(date) && shift.getTime().getStart_time() == startTime && shift.getStudentWorker().equals(studentWorker)) {
                return shift;
            }
        }
        return null; // Shift not found
    }

    public Shift removeShift(Date date, int startTime, StudentWorker studentWorker)
    {
        for (Shift shift : shifts) {
            if (shift.getDate().equals(date) && shift.getTime().getStart_time() == startTime && shift.getStudentWorker().equals(studentWorker)) {
                shifts.remove(shift);
                return shift;
            }
        }
        return null; // Shift not found
    }

    /**
     * Return a copy of the shifts list for read-only listing.
     */
    public List<Shift> getShifts() {
        return new ArrayList<>(shifts);
    }


}
