package com.crossword.utility;


import java.time.LocalDateTime;

public class Profile {
    private Long id;
    private String sessionID;
    private String username;
    private String email;
    private LocalDateTime duration;
    private String password;

    public Profile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Profile(Long id, String sessionID, String username, String email, LocalDateTime duration) {
        this.id = id;
        this.sessionID = sessionID;
        this.username = username;
        this.email = email;
        this.duration = duration;
    }

    public Boolean isSessionGone(LocalDateTime timestamp){
        return (duration.isAfter(timestamp))? true : false;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDuration(LocalDateTime duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDuration() {
        return duration;
    }
}
