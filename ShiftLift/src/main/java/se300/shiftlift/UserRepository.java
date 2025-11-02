package se300.shiftlift;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    //Note that extending JpaRepository gives UserRepository a lot of built in methods already
    
    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.

    //Returns a slice of all Users in the datbase
    Slice<User> findAllBy(Pageable pageable);
    //Returns a list of all users with a given Username
    List<User> findByUsername(String username);
    // Search by username containing (case-insensitive) with paging
    Slice<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    
    // Find users with matching initials
    List<User> findByInitials(String initials);
}
