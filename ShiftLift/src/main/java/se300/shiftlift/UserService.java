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
            userRepository.saveAndFlush(studentWorker);    
        }
        
    }

    //Returns a list of all users in the users database
    @Transactional(readOnly = true)
    public List<User> list(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();
    }
    
    @Transactional(readOnly = true)
    public List<User> findByUsername(String username) {
        List<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return user;
    }
}
