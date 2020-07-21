package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
import java.util.Locale;

public class Expense {

    private final int id;
    private final int payer;
    private final int event;
    private final float value;
    private final String currency;
    private final String object;
    private Date date;

    // For HTML body
    public Expense(@JsonProperty("payer") int payer,
                   @JsonProperty("event") int event,
                   @JsonProperty("value") float value,
                   @JsonProperty("currency") String currency,
                   @JsonProperty("object") String object,
                   @JsonProperty("date") String date) {
        this.id = 0;
        this.payer = payer;
        this.event = event;
        this.value = value;
        this.currency = currency;
        this.object = object;
        if (date.equals("")) this.date = null;
        else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.date = format.parse(date);
            } catch (ParseException e) {
                this.date = new Date();
                e.printStackTrace();
            }
        }
    }

    // For SQL response
    public Expense(int id, int payer, int event, float value, String currency, String object, Date date) {
        this.id = id;
        this.payer = payer;
        this.event = event;
        this.value = value;
        this.currency = currency;
        this.object = object;
        this.date = date;
    }

    // SQL services
    static public List<Expense> GetAllEventExpenses(int eventId, Connection connection) {
        String query = "SELECT * FROM [dbo].[Expenses] WHERE event = " + eventId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Expense> expenses = new ArrayList<>();

            while (resultSet.next()) {
                expenses.add(new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("payer"),
                        resultSet.getInt("event"),
                        resultSet.getFloat("value"),
                        resultSet.getString("currency"),
                        resultSet.getString("object"),
                        resultSet.getDate("date")
                ));
            }
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public List<Expense> GetAllPersonalExpenses(int participantId, Connection connection) {
        String query = "SELECT * FROM [dbo].[Expenses] WHERE payer = " + participantId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Expense> expenses = new ArrayList<>();

            while (resultSet.next()) {
                expenses.add(new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("payer"),
                        resultSet.getInt("event"),
                        resultSet.getFloat("value"),
                        resultSet.getString("currency"),
                        resultSet.getString("object"),
                        resultSet.getDate("date")
                ));
            }
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public Expense FindExpenseById(int id, Connection connection) {
        String query = "SELECT * FROM [dbo].[Expenses] WHERE id = " + id;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("payer"),
                        resultSet.getInt("event"),
                        resultSet.getFloat("value"),
                        resultSet.getString("currency"),
                        resultSet.getString("object"),
                        resultSet.getDate("date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public boolean AddNewExpense(Expense expense, Connection connection) {
        String query = String.format(Locale.US, "INSERT INTO [dbo].[Expenses] (payer, event, value, currency, object) VALUES (%d, %d, %.2f, '%s', '%s')",
                expense.payer, expense.event, expense.value, expense.currency, expense.object);
        if (expense.date != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            query = String.format(Locale.US, "INSERT INTO [dbo].[Expenses] (payer, event, value, currency, object, date) VALUES (%d, %d, %.2f, '%s', '%s', '%s')",
                    expense.payer, expense.event, expense.value, expense.currency, expense.object, format.format(expense.date));
        }
        System.out.println(query);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddThisExpense(Connection connection) {
        return Expense.AddNewExpense(this, connection);
    }


    public int getId() {
        return id;
    }

    public int getPayer() {
        return payer;
    }

    public int getEvent() {
        return event;
    }

    public float getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getObject() {
        return object;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
