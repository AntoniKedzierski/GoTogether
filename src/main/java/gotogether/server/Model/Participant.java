package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Participant {

    private final int id;
    private final int event;
    private final String name;
    private int account;

    private float debt = 0.0f;

    // For HTML body
    public Participant(@JsonProperty("event") int event, @JsonProperty("name") String name, @JsonProperty("account") int account) {
        this.id = 0;
        this.event = event;
        this.name = name;
        this.account = account;
    }

    // For SQL response
    public Participant(int id, int event, String name, int account) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.account = account;
    }

    static public List<Participant> GetAllEventParticipants(int eventId, Connection connection) {
        String query = "SELECT * FROM [dbo].[Participants] WHERE event = " + eventId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Participant> participantList = new ArrayList<>();

            while (resultSet.next()) {
                participantList.add(new Participant(
                        resultSet.getInt("id"),
                        resultSet.getInt("event"),
                        resultSet.getString("name"),
                        resultSet.getInt("account")
                ));
            }
            return participantList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public Participant FindParticipantById(int id, Connection connection) {
        String query = "SELECT * FROM [dbo].[Participants] WHERE id = " + id;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Participant(
                        resultSet.getInt("id"),
                        resultSet.getInt("event"),
                        resultSet.getString("name"),
                        resultSet.getInt("account")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public boolean AddParticipant(Participant participant, Connection connection) {
        String query = String.format("INSERT INTO [dbo].[Participants] (event, name) VALUES (%d, '%s')", participant.event, participant.name);
        if (participant.account >= 0) query = String.format("INSERT INTO [dbo].[Participants] (event, name, account) VALUES (%d, '%s', %d)",
                participant.event, participant.name, participant.account);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddThisParticipant(Connection connection) {
        return Participant.AddParticipant(this, connection);
    }

    public int getId() {
        return id;
    }

    public int getEvent() {
        return event;
    }

    public String getName() {
        return name;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }
}
