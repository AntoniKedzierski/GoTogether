package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Settlement {

    private final int id;
    private final int event;
    private final boolean isClosed;

        // For HTML body
    public Settlement(@JsonProperty("event") int event, @JsonProperty("is_closed") boolean isClosed) {
        this.id = 0;
        this.event = event;
        this.isClosed = isClosed;
    }

    // For SQL response
    public Settlement(int id, int event, boolean isClosed) {
        this.id = id;
        this.event = event;
        this.isClosed = isClosed;
    }

    // SQL service
    static public Settlement FindSettlementById(int id, Connection connection) {
        String query = "SELECT * FROM [dbo].[Settlements] WHERE id = " + id;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Settlement(
                        resultSet.getInt("id"),
                        resultSet.getInt("event"),
                        resultSet.getBoolean("is_closed")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public Settlement FindEventSettlement(int eventId, Connection connection) {
        String query = "SELECT * FROM [dbo].[Settlements] WHERE event = " + eventId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Settlement(
                        resultSet.getInt("id"),
                        resultSet.getInt("event"),
                        resultSet.getBoolean("is_closed")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    static public boolean AddNewSettlement(Settlement settlement, Connection connection) {
        String query = String.format("INSERT INTO [dbo].[Settlements] (event, is_closed) VALUES (%d, %d)",
                settlement.event, settlement.isClosed ? 1 : 0);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddThisSettlement(Connection connection) {
        return Settlement.AddNewSettlement(this, connection);
    }


    public int getId() {
        return id;
    }

    public int getEvent() {
        return event;
    }

    public boolean isClosed() {
        return isClosed;
    }
}
