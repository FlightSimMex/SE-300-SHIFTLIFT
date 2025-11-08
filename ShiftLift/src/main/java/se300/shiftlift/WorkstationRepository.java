package se300.shiftlift;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface WorkstationRepository extends JpaRepository<Workstation, Long>, JpaSpecificationExecutor<Workstation> {
    
Slice<Workstation> findAllBy(Pageable pageable);
List<Workstation> findByWorkstation(String workstation);
Slice<Workstation> findByWorkstationContainingIgnoreCase(String workstation, Pageable pageable);


}
