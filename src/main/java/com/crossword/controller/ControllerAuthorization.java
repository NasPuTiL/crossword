package com.crossword.controller;

import com.crossword.connection.ConnectionProfiles;
import com.crossword.utility.Profile;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class ControllerAuthorization {

    @Value(value = "${com.crossword.driver}")
    private String driver;

    @Value(value = "${com.crossword.url}")
    private String url;

    @Value(value = "${com.crossword.username}")
    private String username;

    @Value(value = "${com.crossword.password}")
    private String password;

    @RequestMapping(method = RequestMethod.GET)
    public String startValue() {
        return "Hello World!";
    }

    @RequestMapping(method = RequestMethod.POST)
    public JSONObject getAuthorization(@RequestBody JSONObject authMap) {
        System.out.println("authMap = " + authMap);
        String sessionid = (String) authMap.get("sessionid");
        if (sessionid == null || sessionid.isEmpty()) {
            JSONObject auth = new JSONObject();
            auth.put("sessionid", null);
            return auth;
        }
        ConnectionProfiles cp = new ConnectionProfiles(driver, url, username, password);
        return cp.getSession(sessionid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public Object setProfile(@RequestBody JSONObject profileMap) {
        System.out.println("IM IN REGISTER METHOD \nprofileMap = " + profileMap);
        if (profileMap == null || profileMap.isEmpty() ||
                profileMap.get("username") == null ||
                profileMap.get("email") == null ||
                profileMap.get("password") == null) {
            JSONObject auth = new JSONObject();
            auth.put("sessionid", null);
            return auth;
        }

        Profile profile = new Profile((String) profileMap.get("username"), (String) profileMap.get("email"), (String) profileMap.get("password"));
        ConnectionProfiles cp = new ConnectionProfiles(driver, url, username, password);
        return cp.register(profile);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get1")
    public String testGETMethodWithoutArguments() {
        return "Return subtitle from GET Method with arguments. I added and resoult.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get2")
    public String testGETMethodWithArguments(int a, int b) {
        return "Return subtitle from GET Method with arguments. I added and resoult = " + a + b;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/post1")
    public String testPOSTMethodWithoutArguments() {
        return "Return subtitle from POST Method without arguments.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/post2")
    public String testPOSTMethodWithArguments(int a, int b) {
        return "Return subtitle from POST Method with arguments. I added and resoult = " + a + b;
    }
}
