package com.example.connection;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.*;

@Service
public class ConnectionProfiles {
    public static PasswordAuthentication getAuthenticator(String username, String password) {
        Connection conn = null;

        String driver_app = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost:5432/cross_word";
        String usernameDb = "postgres";
        String passwordDb = "postgres";

        try {
            Class.forName(driver_app);
            conn = DriverManager.getConnection(url, usernameDb, passwordDb);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String sql = "SELECT counnt(Id) FROM profile where name = ? and pass = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            ps.close();

            while (resultSet.next()){
                int sizeOfQuery = resultSet.getInt(1);
                return (sizeOfQuery == 1)? new PasswordAuthentication(username, password.toCharArray()) : null;
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
