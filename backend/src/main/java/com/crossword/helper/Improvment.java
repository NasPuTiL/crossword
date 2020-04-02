package com.crossword.helper;

import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Improvment {

    public static JSONObject jsonStatementError(String mssg) {
        JSONObject auth = new JSONObject();
        auth.put("error", mssg);
        return auth;
    }

    public static boolean hasNecessaryFields(String username, String email, String password) {
        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean hasNecessaryFieldsLogin(String username, String email, String password) {
        if ((username == null || username.isEmpty()) &&
                (email == null || email.isEmpty()) ||
                password == null || password.isEmpty()) {
            return false;
        }
        return true;
    }
}
