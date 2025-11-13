package se300.shiftlift;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

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
    
}
