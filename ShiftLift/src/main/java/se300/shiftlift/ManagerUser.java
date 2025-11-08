package se300.shiftlift;

import jakarta.persistence.Entity;

@Entity
public class ManagerUser extends User {

    public ManagerUser() {
        // Required for JPA
    }

    public ManagerUser(String email, String password) {
        super(email, password);
        // Admin/Manager users do not participate in seniority; set to 0 as requested
        setSeniorityNumber(0);
    }

    @Override
    public String toString() {
        return "ManagerUser{" + "username='" + getUsername() + '\'' + ", email='" + getEmail() + '\'' + '}';
    }
}
