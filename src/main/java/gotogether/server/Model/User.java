package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private final int id;
    private final String username;
    private final String password;
    private final String email;
    private final Date lastSeen;

    // To create an object from a HTML query
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password,
                @JsonProperty("email") String email) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.email = email;
        this.lastSeen = new Date();
    }

    // To create an object from a SQL response
    public User(int id, String username, String password, String email, Date lastSeen) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.lastSeen = lastSeen;
    }


    // SQL data base methods
    static public List<User> GetAllUsers(Connection connection) {
        String query = "SELECT * FROM [dbo].[Users]";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<User> userList = new ArrayList<>();

            while(resultSet.next()) {
                userList.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getDate("last_login")
                ));
            }

            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public User FindUserById(int id, Connection connection) {
        String query = "SELECT * FROM [dbo].[Users] WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getDate("last_login")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public User FindUserByName(String name, Connection connection) {
        String query = String.format("SELECT * FROM [dbo].[Users] WHERE username = '%s'", name);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getDate("last_login")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public boolean PostUser(User user, Connection connection) {
        String query = String.format("INSERT INTO [dbo].[Users] (username, password, email) VALUES ('%s', '%s', '%s')",
                user.getUsername(), user.getPassword(), user.getEmail());
        if (User.FindUserByName(user.getUsername(), connection) != null) return false;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean PostThisUser(Connection connection) {
        return User.PostUser(this, connection);
    }

    static public boolean ChangePassword(String username, String newPassword, Connection connection) {
        String query = String.format("UPDATE [dbo].[Users] SET password = '%s' WHERE username = '%s'", newPassword, username);
        if (User.FindUserByName(username, connection) == null) return false;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Some cool stuff...
    @Override
    public String toString() {
        StringBuilder strm = new StringBuilder();
        strm.append("ID: ").append(this.id).append("\n");
        strm.append("Username: ").append(this.username).append("\n");
        strm.append("Password: ").append(this.password).append("\n");
        strm.append("Email: ").append(this.email).append("\n");
        strm.append("Last seen: ").append(this.lastSeen).append("\n");
        return strm.toString();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Date getLastSeen() {
        return lastSeen;
    }
}
