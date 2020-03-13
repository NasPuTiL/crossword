package com.crossword.connection;

import com.crossword.utility.Profile;
import org.json.simple.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

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

        String sql = " INTO INTO public.Profile(sessionID, username, password, email, timeExtend) values (?, ?, ?, ?, ?)";
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
            JSONObject auth = new JSONObject();
            if (profile == null) {
                auth.put("sessionid", null);
                return auth;
            }
            auth.put("id", profile.getId());
            auth.put("username", profile.getUsername());
            auth.put("email", profile.getEmail());
            auth.put("sessionId", profile.getSessionID());
            auth.put("duration", profile.getDuration());
            conn.close();
            return auth;
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
                if (profile == null || sesstionExpire(profile.getDuration())) {
                    auth.put("sessionid", null);
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
        return null;
    }

    public JSONObject login(String sessionId) {
        //TODO: add login logic;
        return null;
    }

    private boolean sesstionExpire(LocalDateTime duration) {
        return (duration.isAfter(LocalDateTime.now())) ? true : false;
    }
}
