package com.example.controller;

import com.example.connection.ConnectionProfiles;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.PasswordAuthentication;

@Service
@RestController
@RequestMapping(value = "/rest")
public class ControllerAuthorization {

    @RequestMapping(value = "/authentication")
    public PasswordAuthentication authentication(String username, String password){
        PasswordAuthentication auth = ConnectionProfiles.getAuthenticator(username, password);
        System.out.println("auth = " + auth);

        return auth;
    }

}
