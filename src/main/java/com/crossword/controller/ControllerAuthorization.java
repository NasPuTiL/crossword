package com.crossword.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/home")
public class ControllerAuthorization {

    @RequestMapping(method = RequestMethod.GET, value = "/get1")
    public String testGETMethodWithoutArguments() {
        return "Return subtitle from POST Method with arguments. I added and resoult.";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get2")
    public String testGETMethodWithArguments(int a, int b) {
        return "Return subtitle from POST Method with arguments. I added and resoult = " + a + b;
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
