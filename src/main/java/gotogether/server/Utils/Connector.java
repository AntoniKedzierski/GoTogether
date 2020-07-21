package gotogether.server.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connector {

    private final String username =     "akedzierski";
    private final String password =     "XWee9dain33#";
    private final String dbName =       "GoTogetherDB";
    private final String hostName =     "gotogether.database.windows.net";
    private final String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, username, password);

    private Connection connection;
    private boolean connected = false;

    public Connector() {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connected via URL successfully! Schema: " + connection.getSchema());
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while connecting via URL...");
            e.printStackTrace();
        }
    }

    public Connection Connect() {
        try {
            if (!connected) connection = DriverManager.getConnection(url);
            connected = true;
            return connection;
        } catch (SQLException e) {
            System.out.println("Cannot connect right now...");
            e.printStackTrace();
        }
        return null;
    }

    public boolean Disconnect() {
        try {
            connected = false;
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot disconnect right now...");
            e.printStackTrace();
        }
        return false;
    }
}
