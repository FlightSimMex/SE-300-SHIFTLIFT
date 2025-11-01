package se300.shiftlift;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }

    //Creates a new Student Worker object and immediately adds it to a row in the users database
    @Transactional
    public void createStudentWorker(String email, String password) {
        StudentWorker studentWorker = new StudentWorker(email, password);
       
        if(!findByUsername(studentWorker.getUsername()).isEmpty()) {
            
            throw new IllegalArgumentException("Username already exists");
           
        }else{
            // Determine the current maximum seniority among existing student workers
            int maxSeniority = userRepository.findAll().stream()
                    .filter(u -> u instanceof StudentWorker)
                    .mapToInt(User::getSeniority)
                    .filter(s -> s >= 0) // ignore uninitialized values
                    .max()
                    .orElse(0);

            // New user gets lowest seniority number (max + 1)
            studentWorker.setSeniorityNumber(maxSeniority + 1);

            userRepository.saveAndFlush(studentWorker);
        }
        
    }

    

        

    //Returns a list of all users in the users database
    @Transactional(readOnly = true)
    public List<User> list(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();
    }

    //Returns a list of all students with a certain username
    //Does this have to return a list? I'll test
    @Transactional(readOnly = true)
    public List<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Slice<User> searchByUsername(String username, org.springframework.data.domain.Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    @Transactional
    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void delete(User user) {
        if (user == null) return;
        userRepository.delete(user);
        userRepository.flush();
        
        // After deleting a user, recompute seniority numbers for remaining student workers.
        // Seniority numbers are compressed to consecutive integers starting at 1 where
        // 1 is the most senior (lowest number) and larger numbers are lower seniority.
        List<User> all = userRepository.findAll();

        // Extract StudentWorker instances and sort them by current seniority (ascending).
        List<StudentWorker> students = all.stream()
                .filter(u -> u instanceof StudentWorker)
                .map(u -> (StudentWorker) u)
                .sorted((a, b) -> Integer.compare(a.getSeniority(), b.getSeniority()))
                .toList();

        // Reassign seniority sequentially starting at 1.
        int seniority = 1;
        for (StudentWorker sw : students) {
            sw.setSeniorityNumber(seniority);
            seniority++;
            userRepository.save(sw);
        }
        userRepository.flush();
    }

    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
}
