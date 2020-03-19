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
            ps.executeUpdate();
            ps.close();

            if (profile == null) {
                JSONObject auth = new JSONObject();
                auth.put("sessionid", null);
                return auth;
            }
            return getProfileByUserName(profile.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                    JSONObject authFaile = new JSONObject();
                    authFaile.put("sessionid", null);
                    return authFaile;
                }
                conn.close();
                return auth;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                    auth.put("sessionid", "Session expired");
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
        JSONObject auth = new JSONObject();
        auth.put("sessionid", null);
        return auth;
    }

    public JSONObject login(String sessionId) {
        //TODO: add login logic;
        return null;
    }

    private boolean sessionExpire(LocalDateTime duration) {
        return (duration.isBefore(LocalDateTime.now())) ? true : false;
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
                    JSONObject obj = new JSONObject();
                    obj.put("resoult", null);
                    authFaile.put(Integer.valueOf(0), obj);
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

}
