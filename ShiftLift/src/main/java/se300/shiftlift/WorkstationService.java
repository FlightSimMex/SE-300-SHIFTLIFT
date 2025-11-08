package se300.shiftlift;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkstationService {
    
    private final WorkstationRepository workstationRepository;
    
    public WorkstationService(WorkstationRepository workstationRepository) {
        this.workstationRepository = workstationRepository;
    }
    
    
    @Transactional
    public void createWorstation(String name)
    {
        Workstation workstation = new Workstation(name);

        if(!findByName(workstation.getName()).isEmpty()){
            throw new IllegalArgumentException("Workstation already exists");
        }else{
            workstationRepository.saveAndFlush(workstation);
            //TODO: Check if has operation hours, if not set default??
        }

    }

    //Returns list of all workstations in database
    @Transactional(readOnly = true)
    public List<Workstation> list(Pageable pageable) {
        return workstationRepository.findAll(pageable).toList();
    }

    //Returns a list of all workstations with a certain name
    @Transactional(readOnly = true)
    public List<Workstation> findByName(String name) {
        return workstationRepository.findByWorkstation(name);
    }
    
    //Returns Optional of workstation by name for EditWorkstationView
    @Transactional(readOnly = true)
    public java.util.Optional<Workstation> findByNameOptional(String name) {
        List<Workstation> workstations = workstationRepository.findByWorkstation(name);
        return workstations.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(workstations.get(0));
    }

    //Returns workstation from database matching name search
    @Transactional(readOnly = true)
    public Slice<Workstation> searchByName(String name, Pageable pageable) {
        return workstationRepository.findByWorkstationContainingIgnoreCase(name, pageable);
    }

    //Saves or updates a workstation in the database
    @Transactional
    public Workstation save(Workstation workstation)
    {
        return workstationRepository.saveAndFlush(workstation);
    }

    //Deletes a workstation from the database
    @Transactional
    public void delete(Workstation workstation) {
        if (workstation == null) return;
        workstationRepository.delete(workstation);
        workstationRepository.flush();
    }

    //Return number of workstations in database
    @Transactional(readOnly = true)
    public long count() {
        return workstationRepository.count();
    }

    

   
}