package com.crossword.controller;

import com.crossword.connection.ConnectionProfiles;
import com.crossword.helper.Improvment;
import com.crossword.utility.Profile;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*")
public class ControllerAuthorization {

    @Value(value = "${com.crossword.driver}")
    private String driver;

    @Value(value = "${com.crossword.url}")
    private String url;

    @Value(value = "${com.crossword.username}")
    private String username;

    @Value(value = "${com.crossword.password}")
    private String password;

    @RequestMapping(method = RequestMethod.POST)
    public JSONObject getAuthorization(@RequestBody JSONObject authMap) {
        System.out.println("@getAuthorization\njson = " + authMap);
        String sessionid = (String) authMap.get("sessionid");
        if (sessionid == null || sessionid.isEmpty()) {
            return Improvment.jsonStatementError("There has no authentication Session");
        }
        ConnectionProfiles cp = new ConnectionProfiles(driver, url, username, password);
        return cp.getSession(sessionid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public JSONObject register(@RequestBody JSONObject profileMap) {
        System.out.println("@register\njson = " + profileMap);
        String username = (String) profileMap.get("username");
        String email = (String) profileMap.get("email");
        String password = (String) profileMap.get("password");

        if (!Improvment.hasNecessaryFieldsRegister(username, email, password)) {
            return Improvment.jsonStatementError("Require fields : username, email, password");
        }

        Profile profile = new Profile(username, email, password);
        ConnectionProfiles cp = new ConnectionProfiles(this.driver, this.url, this.username, this.password);
        return cp.register(profile);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public JSONObject login(@RequestBody JSONObject profileMap) {
        System.out.println("@login\njson = " + profileMap);
        String username = (String) profileMap.get("username");
        String email = (String) profileMap.get("email");
        String password = (String) profileMap.get("password");

        if (!Improvment.hasNecessaryFieldsLogin(username, email, password)) {
            return Improvment.jsonStatementError("Require fields: password and username or mail");
        }

        Profile profile = new Profile(username, email, password);
        ConnectionProfiles cp = new ConnectionProfiles(this.driver, this.url, this.username, this.password);
        return cp.login(profile);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUsers")
    public Map<Integer, JSONObject> getUsers() {
        System.out.println("@getUsers");
        ConnectionProfiles cp = new ConnectionProfiles(driver, url, username, password);
        return cp.getAllUsers();
    }
}
