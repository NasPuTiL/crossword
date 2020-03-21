package com.crossword.connection;

import com.crossword.utility.Profile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ConnectionProfiles {
    private static final java.util.UUID UUID = new UUID(28, 35);
    private static Connection conn;

    public ConnectionProfiles(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JSONObject register(Profile profile) {
        if(!checkUniqName(profile.getUsername())){
            return jsonStatementError("User on this username is already exists");
        }

        String sql = "INSERT INTO public.Profile(sessionid, username, password, email, duration) values (?, ?, ?, ?, ?)";
        try {
            LocalDateTime localDate = LocalDateTime.now().plusMinutes(10);
            String uniqueID = UUID.randomUUID().toString();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uniqueID);
            ps.setString(2, profile.getUsername());
            ps.setString(3, profile.getPassword());
            ps.setString(4, profile.getEmail());
            ps.setObject(5, localDate);
            int result = ps.executeUpdate();
            ps.close();
            if (result != 1) {
                return jsonStatementError("Can't add to database profile with this values");
            }

            return getProfileByUserName(profile.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonStatementError("Something go wrong with register profile");
    }

    private JSONObject getProfileByUserName(String username) {
        JSONObject auth = new JSONObject();
        String sql = "SELECT id, username, email, sessionid, duration from public.profile where username = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                auth.put("id", resultSet.getString(1));
                auth.put("username", resultSet.getString(2));
                auth.put("email", resultSet.getString(3));
                auth.put("sessionId", resultSet.getString(4));
                auth.put("duration", resultSet.getString(5));

                if (auth.get("id") == null) {
                    return jsonStatementError("Some problem with getProfileByUserName, username: " + username);
                }
            }
            conn.close();
            return auth;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonStatementError("There is no profile on this username: " + username);
    }

    public JSONObject getSession(String sessionId) {
        String sql = "SELECT id, username, email, sessionId, duration FROM profile where sessionId = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Profile profile = new Profile(resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getObject(5, LocalDateTime.class));

                JSONObject auth = new JSONObject();
                if (profile == null || sessionExpire(profile.getDuration())) {
                    jsonStatementWarn("Session expired");
                    return auth;
                }

                auth.put("id", profile.getId());
                auth.put("username", profile.getUsername());
                auth.put("email", profile.getEmail());
                auth.put("sessionId", profile.getSessionID());
                auth.put("duration", profile.getDuration());
                conn.close();
                return auth;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jsonStatementError("Something go wrong with getting session");
    }

    public JSONObject login(Profile profile) {
        boolean isUsername = false;
        String sql = "SELECT username from profile where password = ? and ";
        sql +=(profile.getEmail() == null)?"username = ? LIMIT 1" : "email = ? LIMIT 1";
        String username = null;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, profile.getPassword());
            if(profile.getEmail() == null || profile.getEmail().isEmpty()){
                isUsername = true;
                ps.setString(2, profile.getUsername());
            }else {
                ps.setString(2, profile.getEmail());
            }

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString(1);
            }

            if(username == null || username.isEmpty()){
                if(isUsername){
                    return jsonStatementError("Wrong credential, username = " + profile.getUsername() + " and password = " + profile.getPassword());
                }else{
                    return jsonStatementError("Wrong credential, email = " + profile.getEmail() + " and password = " + profile.getPassword());
                }
            }

            int result;
            if((result = refreshSession(profile.getUsername())) != 1){
                return jsonStatementError("duration update end not successful: " + result);
            }
            return getProfileByUserName(username);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonStatementError("Some problem with getting login");
    }

    public Map<Integer, JSONObject> getAllUsers() {
        Map<Integer, JSONObject> authInc = new HashMap<>();
        String sql = "SELECT id, username, password, email, sessionid, duration from public.profile";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            int x = 0;
            while (resultSet.next()) {
                JSONObject auth = new JSONObject();
                auth.put("id", resultSet.getString(1));
                auth.put("username", resultSet.getString(2));
                auth.put("password", resultSet.getString(3));
                auth.put("email", resultSet.getString(4));
                auth.put("sessionId", resultSet.getString(5));
                auth.put("duration", resultSet.getString(6));
                authInc.put(Integer.valueOf(x), auth);

                if (auth.get("id") == null) {
                    Map<Integer, JSONObject> authFaile = new HashMap<>();
                    authFaile.put(Integer.valueOf(0), jsonStatementError("Not working... ID: " + x));
                    return authFaile;
                }
                x++;
            }
            conn.close();
            return authInc;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<Integer, JSONObject> authFaile = new HashMap<>();
        authFaile.put(Integer.valueOf(0), null);
        return authFaile;
    }


    private boolean sessionExpire(LocalDateTime duration) {
        return (duration.isBefore(LocalDateTime.now())) ? true : false;
    }

    private boolean checkUniqName(String username) {
        String sql = "SELECT count(id) from public.profile WHERE username = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int result = resultSet.getInt(1);
                if(result > 0){
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private int refreshSession(String username) {
        String sql = "UPDATE public.profile SET duration = ? WHERE username = ?";
        try {
            LocalDateTime localDate = LocalDateTime.now().plusMinutes(10);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, localDate);
            preparedStatement.setString(2, username);
            int resultSet = preparedStatement.executeUpdate();

            if(resultSet != 1){
                System.out.println("UPDATE duration return code: " + resultSet);
            }
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

        private JSONObject jsonStatementError(String mssg) {
        JSONObject auth = new JSONObject();
        auth.put("error", mssg);
        return auth;
    }

    private JSONObject jsonStatementWarn(String mssg) {
        JSONObject auth = new JSONObject();
        auth.put("warn", mssg);
        return auth;
    }

    private JSONObject jsonStatementInfo(String mssg) {
        //TODO: implement body
        return null;
    }
}
