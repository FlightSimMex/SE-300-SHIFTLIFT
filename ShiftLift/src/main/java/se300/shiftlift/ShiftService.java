package se300.shiftlift;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShiftService 
{
    private ShiftRepositry shiftRepositry;

    ShiftService(ShiftRepositry shiftRepositry) {
        this.shiftRepositry = shiftRepositry;
        
    }

    @Transactional
    public void addShift(Date date, User worker, Workstation workstation, Time time)
    {
        try {
            Shift shift = new Shift(date, time, workstation, worker);
            shiftRepositry.saveAndFlush(shift);
        } catch (Exception e) {
            System.out.println("Error adding shift: " + e.getMessage());
        }
 
    }

    //Returns a list of all shifts in the database
    @Transactional(readOnly = true)
    public List<Shift> getAllShifts() {
        return shiftRepositry.findAll();
    }

    @Transactional
    public void updateShift(Shift shift, Date date, User worker, Workstation workstation, Time time) {
        try {
            shift.changeDate(date);
            shift.changeStudentWorker((StudentWorker) worker);
            shift.changeWorkstation(workstation);
            shift.changeTime(time);
            shiftRepositry.saveAndFlush(shift);
        } catch (Exception e) {
            System.out.println("Error updating shift: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteShift(Shift shift) {
        try {
            if (shift != null) {
                shiftRepositry.delete(shift);
                shiftRepositry.flush();
            }
        } catch (Exception e) {
            System.out.println("Error deleting shift: " + e.getMessage());
        }
    }

    public boolean workstationOcupied(Workstation workstation, Date date, Time time) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            // Compare workstation by ID (for entities) and date by value
            boolean sameWorkstation = shift.getWorkstation().getId() != null && 
                                     workstation.getId() != null &&
                                     shift.getWorkstation().getId().equals(workstation.getId());
            
            boolean sameDate = shift.getDate().get_Date() == date.get_Date();
            
            if (sameWorkstation && sameDate && timesOverlap(shift.getTime(), time)) {
                return true; // Workstation is occupied (times overlap)
            }
        }
        return false; // Workstation is available
    }

    public boolean workstationOcupied(Workstation workstation, Date date, Time time, Long excludeShiftId) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            // Skip the shift being edited
            if (excludeShiftId != null && shift.getId() != null && shift.getId().equals(excludeShiftId)) {
                continue;
            }
            
            // Compare workstation by ID (for entities) and date by value
            boolean sameWorkstation = shift.getWorkstation().getId() != null && 
                                     workstation.getId() != null &&
                                     shift.getWorkstation().getId().equals(workstation.getId());
            
            boolean sameDate = shift.getDate().get_Date() == date.get_Date();
            
            if (sameWorkstation && sameDate && timesOverlap(shift.getTime(), time)) {
                return true; // Workstation is occupied (times overlap)
            }
        }
        return false; // Workstation is available
    }

    public boolean workerDoubleBooked(User worker, Date date, Time time) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            // Compare worker by ID (for entities) and date by value
            boolean sameWorker = shift.getStudentWorker().getId() != null && 
                                worker.getId() != null &&
                                shift.getStudentWorker().getId().equals(worker.getId());
            
            boolean sameDate = shift.getDate().get_Date() == date.get_Date();
            
            if (sameWorker && sameDate && timesOverlap(shift.getTime(), time)) {
                return true; // Worker is double booked (times overlap)
            }
        }
        return false; // Worker is available
    }

    public boolean workerDoubleBooked(User worker, Date date, Time time, Long excludeShiftId) {
        List<Shift> allShifts = getAllShifts();
        for (Shift shift : allShifts) {
            // Skip the shift being edited
            if (excludeShiftId != null && shift.getId() != null && shift.getId().equals(excludeShiftId)) {
                continue;
            }
            
            // Compare worker by ID (for entities) and date by value
            boolean sameWorker = shift.getStudentWorker().getId() != null && 
                                worker.getId() != null &&
                                shift.getStudentWorker().getId().equals(worker.getId());
            
            boolean sameDate = shift.getDate().get_Date() == date.get_Date();
            
            if (sameWorker && sameDate && timesOverlap(shift.getTime(), time)) {
                return true; // Worker is double booked (times overlap)
            }
        }
        return false; // Worker is available
    }
    

    private boolean timesOverlap(Time t1, Time t2) {
        return t1.getStart_time() < t2.getEnd_time() && t2.getStart_time() < t1.getEnd_time();
    }
}
