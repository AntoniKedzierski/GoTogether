package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {

    private final int id;
    private final int owner;
    private final String name;
    private final String destination;
    private Date beginDate;
    private Date endDate;
    private final boolean hasEnded;

    // For HTML body
    public Event(@JsonProperty("owner") @NotNull int owner,
                 @JsonProperty("name") @NotNull String name,
                 @JsonProperty("destination") String destination,
                 @JsonProperty("begin_date") @NotNull String beginDate,
                 @JsonProperty("end_date") String endDate,
                 @JsonProperty("has_ended") @NotNull boolean hasEnded) {
        this.id = 0;
        this.owner = owner;
        this.name = name;
        this.destination = destination.equals("") ? null : destination;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.beginDate = format.parse(beginDate);
            if (!endDate.equals("")) {
                this.endDate = format.parse(endDate);
            }
            else this.endDate = null;
        } catch (ParseException e) {
            this.beginDate = new Date();
            this.endDate = null;
            e.printStackTrace();
        }
        this.hasEnded = hasEnded;
    }

    // For SQL response
    public Event(int id, int owner, String name, String destination, Date beginDate, Date endDate, boolean hasEnded) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.destination = destination;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.hasEnded = hasEnded;
    }


    // SQL services
    static public List<Event> GetAllEvents(Connection connection) {
        String query = "SELECT * FROM [dbo].[Events]";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Event> eventList = new ArrayList<>();

            while(resultSet.next()) {
                eventList.add(new Event(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner"),
                        resultSet.getString("name"),
                        resultSet.getString("destination"),
                        resultSet.getDate("begin_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBoolean("has_ended")
                ));
            }

            return eventList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public List<Event> GetAllUserEvents(String username, Connection connection) {
        String query = String.format("SELECT events.* FROM [dbo].[Events] AS events, [dbo].[Users] AS users " +
                "WHERE events.owner = users.id AND users.username = '%s'", username);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Event> eventList = new ArrayList<>();

            while(resultSet.next()) {
                eventList.add(new Event(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner"),
                        resultSet.getString("name"),
                        resultSet.getString("destination"),
                        resultSet.getDate("begin_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBoolean("has_ended")
                ));
            }

            return eventList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public Event FindEventById(int id, Connection connection) {
        String query = "SELECT * FROM [dbo].[Events] WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Event(
                        resultSet.getInt("id"),
                        resultSet.getInt("owner"),
                        resultSet.getString("name"),
                        resultSet.getString("destination"),
                        resultSet.getDate("begin_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBoolean("has_ended")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    static public boolean AddNewEvent(Event event, Connection connection) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String query = String.format("INSERT INTO [dbo].[Events] (owner, name, begin_date, has_ended) VALUES (%d, '%s', '%s', %d)",
                    event.getOwner(), event.getName(), dateFormat.format(event.getBeginDate()), event.isHasEnded() ? 1 : 0);
        if (event.endDate == null && event.destination != null) {
            query = String.format("INSERT INTO [dbo].[Events] (owner, name, destination, begin_date, has_ended) VALUES (%d, '%s', '%s', '%s', %d)",
                    event.getOwner(), event.getName(), event.getDestination(), dateFormat.format(event.getBeginDate()), event.isHasEnded() ? 1 : 0);
        }
        if (event.endDate != null && event.destination == null) {
            query = String.format("INSERT INTO [dbo].[Events] (owner, name, begin_date, end_date, has_ended) VALUES (%d, '%s', '%s', '%s', %d)",
                    event.getOwner(), event.getName(), dateFormat.format(event.getBeginDate()), dateFormat.format(event.getEndDate()), event.isHasEnded() ? 1 : 0);
        }
        if (event.endDate != null && event.destination != null) {
            query = String.format("INSERT INTO [dbo].[Events] (owner, name, destination, begin_date, end_date, has_ended) VALUES (%d, '%s', '%s', '%s', '%s', %d)",
                    event.getOwner(), event.getName(), event.getDestination(), dateFormat.format(event.getBeginDate()), dateFormat.format(event.getEndDate()), event.isHasEnded() ? 1 : 0);
        }

        // System.out.println(query);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean PostThisEvent(Connection connection) {
        return Event.AddNewEvent(this, connection);
    }



    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isHasEnded() {
        return hasEnded;
    }
}
