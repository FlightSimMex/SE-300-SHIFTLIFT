package se300.shiftlift;

import java.util.ArrayList;
import java.util.List;

public class Day {

    private Date date;
    private List<Shift> shifts;

    public Day(Date date) {
        this.date = date;
        this.shifts = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void addShift(Shift shift) {
        shifts.add(shift);
    }

    public void removeShift(Shift shift) {
        shifts.remove(shift);
    }

    public void findShift(User worker, Time time)
    {
        for (Shift shift : shifts) {
            if (shift.getStudentWorker().equals(worker) &&
                shift.getTime().getStart_time() == time.getStart_time() &&
                shift.getTime().getEnd_time() == time.getEnd_time()) {
                shifts.remove(shift);
                return;
            }
        }
    }

    public boolean isWorkstationOccupied(Workstation workstation, Time checkTime) {
        if (workstation == null || checkTime == null) {
            return false;
        }
        
        for (Shift shift : shifts) {
            if (shift.getWorkstation().equals(workstation)) {
                // Check if there's any time overlap between the shift and the check time
                if (timesOverlap(shift.getTime(), checkTime)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isWorkstationOccupied(Workstation workstation, int startTime, int endTime) {
        if (workstation == null) {
            return false;
        }
        
        try {
            Time checkTime = new Time(startTime, endTime);
            return isWorkstationOccupied(workstation, checkTime);
        } catch (IllegalArgumentException e) {
            // Invalid time range provided
            return false;
        }
    }
    
    private boolean timesOverlap(Time time1, Time time2) {
        int start1 = time1.getStart_time();
        int end1 = time1.getEnd_time();
        int start2 = time2.getStart_time();
        int end2 = time2.getEnd_time();
        
        // Two time periods overlap if:
        // start1 < end2 AND start2 < end1
        return start1 < end2 && start2 < end1;
    }

    public boolean isPersonScheduled(User worker, Time checkTime) {
        if (worker == null || checkTime == null) {
            return false;
        }
        
        for (Shift shift : shifts) {
            if (shift.getStudentWorker().equals(worker)) {
                // Check if there's any time overlap between the shift and the check time
                if (timesOverlap(shift.getTime(), checkTime)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isPersonScheduled(User worker, int startTime, int endTime) {
        if (worker == null) {
            return false;
        }
        
        try {
            Time checkTime = new Time(startTime, endTime);
            return isPersonScheduled(worker, checkTime);
        } catch (IllegalArgumentException e) {
            // Invalid time range provided
            return false;
        }
    }
    
    public List<Shift> getPersonShiftsDuringTime(User worker, Time checkTime) {
        List<Shift> overlappingShifts = new ArrayList<>();
        
        if (worker == null || checkTime == null) {
            return overlappingShifts;
        }
        
        for (Shift shift : shifts) {
            if (shift.getStudentWorker().equals(worker)) {
                if (timesOverlap(shift.getTime(), checkTime)) {
                    overlappingShifts.add(shift);
                }
            }
        }
        
        return overlappingShifts;
    }
    
}
