package com.crossword.utility;


import java.sql.Timestamp;

public class Profile {
    private Long id;
    private String sessionID;
    private String username;
    private String email;
    private Timestamp timeExtend;
    private String password;

    public Profile(Long id, String sessionID, String username, String password, String email, Timestamp timeExtend) {
        this.id = id;
        this.sessionID = sessionID;
        this.username = username;
        this.email = email;
        this.timeExtend = timeExtend;
        this.password = password;
    }

    public Boolean isSessionGone(Timestamp timestamp){
        return (timeExtend.after(timestamp))? true : false;
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

    public void setTimeExtend(Timestamp timeExtend) {
        this.timeExtend = timeExtend;
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

    public Timestamp getTimeExtend() {
        return timeExtend;
    }
}
