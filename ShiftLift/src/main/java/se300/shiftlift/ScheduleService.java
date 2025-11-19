package se300.shiftlift;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ShiftRepositry shiftRepositry;

    ScheduleService(ScheduleRepository scheduleRepository, ShiftRepositry shiftRepositry) {
        this.scheduleRepository = scheduleRepository;
        this.shiftRepositry = shiftRepositry;
    }

    @Transactional
    public Schedule createSchedule(Date startDate, Date endDate) {
        Schedule schedule = new Schedule(startDate, endDate);
        return scheduleRepository.saveAndFlush(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Transactional
    public Schedule save(Schedule schedule) {
        return scheduleRepository.saveAndFlush(schedule);
    }

    @Transactional
    public void delete(Schedule schedule) {
        if (schedule == null) return;
        scheduleRepository.delete(schedule);
        scheduleRepository.flush();
    }

    @Transactional
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
        scheduleRepository.flush();
    }

    @Transactional(readOnly = true)
    public long count() {
        return scheduleRepository.count();
    }

    /**
     * Get the latest unpublished (not approved) schedule.
     * Returns the most recently created schedule that has not been approved yet.
     */
    @Transactional(readOnly = true)
    public Optional<Schedule> getLatestUnpublishedSchedule() {
        List<Schedule> allSchedules = scheduleRepository.findAll();
        return allSchedules.stream()
            .filter(s -> s.getApproved() == null || !s.getApproved())
            .max((s1, s2) -> {
                // Compare by ID (assuming higher ID = more recent)
                if (s1.getId() == null) return -1;
                if (s2.getId() == null) return 1;
                return s1.getId().compareTo(s2.getId());
            });
    }

    /**
     * Load shifts for a schedule from the database.
     * This method finds all shifts that fall within the schedule's date range.
     */
    @Transactional
    public void loadShiftsForSchedule(Schedule schedule) {
        List<Shift> allShifts = shiftRepositry.findAll();
        
        int startDate = schedule.getStartDate().get_Date();
        int endDate = schedule.getEndDate().get_Date();
        
        schedule.getShifts().clear();
        
        for (Shift shift : allShifts) {
            int shiftDate = shift.getDate().get_Date();
            if (shiftDate >= startDate && shiftDate <= endDate) {
                schedule.getShifts().add(shift);
            }
        }
        
        scheduleRepository.saveAndFlush(schedule);
    }
}
