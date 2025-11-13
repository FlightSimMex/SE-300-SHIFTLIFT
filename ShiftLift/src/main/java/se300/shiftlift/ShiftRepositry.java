package se300.shiftlift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ShiftRepositry extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> 
{


    

}
