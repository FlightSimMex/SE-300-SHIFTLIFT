package com.example.application;

public abstract class User {

    private String username;
    private String initials;
    private String email;
    private String password;

    public  User(String email, String password)
    {
        if(email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email, password, and name cannot be null or empty");
        }else{
            this.email = email;
            this.password = password;

            String [] emailParts = email.split("@");
            this.username = emailParts[0];
            this.initials = get_first_inital(emailParts[0])+emailParts[0].charAt(0);
            
        }
        
    }

    private String get_first_inital(String username)
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

    

}
