package com.crossword.connection;

import com.crossword.mailAuthentication.SimpleMailSend;
import com.crossword.utility.Profile;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
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
        if (!checkUniqName(profile.getUsername())) {
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

            //TODO: add smtp server to send mail
/*            SimpleMailSend mailSend = new SimpleMailSend();
            mailSend.sendMessage(profile.getEmail(), uniqueID, profile.getUsername());*/
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
                auth.put("token", resultSet.getString(4));
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
                    auth = jsonStatementWarn("Session expired");
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
        sql += (profile.getEmail() == null) ? "username = ? LIMIT 1" : "email = ? LIMIT 1";
        String username = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, profile.getPassword());
            if (profile.getEmail() == null || profile.getEmail().isEmpty()) {
                isUsername = true;
                ps.setString(2, profile.getUsername());
            } else {
                ps.setString(2, profile.getEmail());
            }

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString(1);
            }

            if (username == null || username.isEmpty()) {
                if (isUsername) {
                    return jsonStatementError("Wrong credential, username = " + profile.getUsername() + " and password = " + profile.getPassword());
                } else {
                    return jsonStatementError("Wrong credential, email = " + profile.getEmail() + " and password = " + profile.getPassword());
                }
            }

            int result;
            if ((result = refreshSession(profile.getUsername())) != 1) {
                return jsonStatementError("duration update end not successful: " + result);
            }
            return getProfileByUserName(username);

        } catch (SQLException e) {
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

            while (resultSet.next()) {
                int result = resultSet.getInt(1);
                if (result > 0) {
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

            if (resultSet != 1) {
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

    private JSONObject jsonStatementOK(String mssg) {
        JSONObject auth = new JSONObject();
        auth.put("OK", mssg);
        return auth;
    }

    public Map<String, JSONObject> setKeyAndValues(JSONObject json) {
        Collection s = json.values();
        List<List<String>> valuesDoubleList = new LinkedList<>(s);
        List valuesList = valuesDoubleList.get(0);
        Object keyy = valuesDoubleList.get(1);
        if (keyy == null) {
            jsonStatementError("key invalid or empty");
        }
        String key = (String) keyy;
        if (key.isEmpty()) {
            jsonStatementError("key invalid or empty");
        }
        if (valuesList == null || valuesList.isEmpty() || valuesList.size() < 1) {
            jsonStatementError("values invalid or empty");
        }

        if (setKey(key) != 1) {
            jsonStatementError("Issue with added key");
        }
        int keyID = findValueID(key);
        setValues(valuesList, keyID);

        return jsonStatementOK("done");
    }

    private int findValueID(String key) {
        String sql = "SELECT Id from key_word where key = ? LIMIT 1";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ResultSet resultSet = ps.executeQuery();

            int id = 0;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    private int setValues(List<String> valuesList, int keyID) {
        String sql = "insert into value_word (key_word__fk , value ) values (?, ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (String value : valuesList) {
                ps.setInt(1, keyID);
                ps.setString(2, value);
                int result = ps.executeUpdate();
                if (result != 1) {
                    System.out.println("error! incorrect value = " + value);
                    return -1;
                }
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    private int setKey(String key) {
        String sql = "insert into key_word (key, power ) values (?, 1);";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            int result = ps.executeUpdate();
            ps.close();
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public Map<String, JSONObject> findResult(JSONObject json) {
        Map<String, JSONObject> results = new HashMap<>();

        List<String> valuesList = (List<String>) json.get("values");
        if (valuesList == null || valuesList.isEmpty() || valuesList.size() < 1) {
            jsonStatementError("values invalid or empty");
        }

        String sql = "select kw.key, COUNT(vw.key_word__fk) from key_word kw " +
                "left join value_word vw on kw.id = vw.key_word__fk where";
        for (int i = 0; i < valuesList.size(); i++) {
            sql += " vw.value like ?";
            if (i < valuesList.size() - 1) {
                sql += " or ";
            }
        }
        sql += " GROUP by kw.key  order by COUNT(vw.key_word__fk) DESC;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 1; i <= valuesList.size(); i++) {
                ps.setString(i, addPercent(valuesList.get(i - 1)));
            }
            ResultSet resultSet = ps.executeQuery();
            int x = 0;
            while (resultSet.next()) {
                JSONObject auth = new JSONObject();

                int numb = resultSet.getInt(2);
                auth.put("occurrences", numb);

                String word = resultSet.getString(1);
                auth.put("key", word);

                results.put("obj[" + Integer.valueOf(x) + "]", auth);
                x++;
            }
            System.out.println("results = " + results);
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return jsonStatementError("find not working correctly");
    }

    private String addPercent(String word) {
        return "%" + word + "%";
    }

    public JSONObject removeUser(JSONObject json) {
        String id = (String) json.get("id");
        String username = (String) json.get("username");

        if (id == null || username == null || id.equals("") || username.equals("")) {
            return jsonStatementError("Remove user process failure");
        }

        String sql = "DELETE FROM profile WHERE id = ? and username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, username);

            int result = ps.executeUpdate();
            ps.close();

            System.out.println("result = " + result);
            if (result != 1) {
                return jsonStatementError("Remove user process failure..");
            }

            return jsonStatementOK("User " + username + " was removed");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return jsonStatementError("Remove user process failure.");
    }
}
