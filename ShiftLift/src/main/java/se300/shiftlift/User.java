package se300.shiftlift;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") //Name of the table that user information is stored in in the database
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id") //Name of a specific column in the database that corresponds with the variable below
    private Long id; //ID required for mySQL work
    
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "initials")
    private String initials;

    @Column(name = "seniority")
    private int seniority;

    protected User() { // Required for Hibernate
    }

    public User(String email, String password)
    {
        if(email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email, password, and name cannot be null or empty");
        }else{
            this.email = email;
            this.password = password;

            String [] emailParts = email.split("@");
            this.username = emailParts[0];
            // Initials will be set by service layer to ensure uniqueness
            this.initials = "";
            this.seniority = -1;
        }
    }

    protected static String get_first_inital(String username)
    {
        if (username == null || username.isEmpty()) {
            return "";
        }

        for (int i = username.length() - 1; i >= 0; i--) {
            char c = username.charAt(i);
            if (Character.isLetter(c)) {
                return String.valueOf(c);
            }
        }

        return "";
    }

    public String getUsername() {
        return username;
    }

    public String getInitials() {
        return initials;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getSeniority() {
        return seniority;
    }




    // Small setters to support editing from CLI
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("password cannot be null or empty");
        this.password = password;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("email cannot be null or empty");
        this.email = email;
        
        // Update username when email changes
        String[] emailParts = email.split("@");
        this.username = emailParts[0];
        // Initials will be updated by service layer
    }

    // Allow service layer to set initials
    void setInitials(String initials) {
        this.initials = initials;
    }

    public void setSeniorityNumber(int seniority) {
        this.seniority = seniority;
    }

    

}
