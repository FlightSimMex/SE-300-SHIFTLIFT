package se300.shiftlift;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    //Note that extending JpaRepository gives UserRepository a lot of built in methods already
    
    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<User> findAllBy(Pageable pageable);
}
