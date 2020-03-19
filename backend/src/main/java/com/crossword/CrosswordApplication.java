package com.crossword;

import com.crossword.controller.ControllerAuthorization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = ControllerAuthorization.class)
public class CrosswordApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrosswordApplication.class, args);
    }
}
