package com.crossword.connection;

import com.crossword.utility.Profile;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class ConnectionProfiles {
    private static Connection conn;

    public ConnectionProfiles(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
            this.conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setProfile(Profile profile) {
        String sql = "INSERT INTO public.Profile(Id, sessionID, username, password, email, timeExtend)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, profile.getId());
            ps.setString(2, profile.getSessionID());
            ps.setString(3, profile.getUsername());
            ps.setString(3, profile.getPassword());
            ps.setString(4, profile.getEmail());
            ps.setTimestamp(5, profile.getTimeExtend());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSession(String username, String password) {

        String sql = "SELECT count(id), sessionId FROM profile where username = ? and password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(1, password);
            ResultSet resultSet = ps.executeQuery();

            String sessionId = null;
            while (resultSet.next()) {
                int res = resultSet.getInt(1);

                if (res == 1) {
                    sessionId = resultSet.getString(2);
                    return sessionId;
                } else {
                    return "0";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
