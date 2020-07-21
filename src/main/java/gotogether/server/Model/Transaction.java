package gotogether.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Transaction {

    private final int id;
    private final int settlement;
    private final int payer;
    private final int recipient;
    private final float value;
    private final String currency;

    // For HTML body
    public Transaction(@JsonProperty("settlement") int settlement,
                       @JsonProperty("payer") int payer,
                       @JsonProperty("recipient") int recipient,
                       @JsonProperty("value") float value,
                       @JsonProperty("currency") String currency) {
        this.id = 0;
        this.settlement = settlement;
        this.payer = payer;
        this.recipient = recipient;
        this.value = value;
        this.currency = currency;
    }

    // For SQL response
    public Transaction(int id, int settlement, int payer, int recipient, float value, String currency) {
        this.id = id;
        this.settlement = settlement;
        this.payer = payer;
        this.recipient = recipient;
        this.value = value;
        this.currency = currency;
    }

    // SQL service
    static public List<Transaction> GetAllTransactions(int settlementId, Connection connection) {
        String query = "SELECT * FROM [dbo].[Transactions] WHERE settlement = " + settlementId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<Transaction> transactions = new ArrayList<>();

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getInt("settlement"),
                        resultSet.getInt("payer"),
                        resultSet.getInt("recipient"),
                        resultSet.getFloat("value"),
                        resultSet.getString("currency")
                ));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public boolean AddNewTransaction(Transaction transaction, Connection connection) {
        String query = String.format(Locale.US, "INSERT INTO [dbo].[Transactions] (settlement, payer, recipient, value, currency) VALUES (%d, %d, %d, %.2f, '%s'",
                transaction.settlement, transaction.payer, transaction.recipient, transaction.value, transaction.currency);

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean AddThisTransaction(Connection connection) {
        return Transaction.AddNewTransaction(this, connection);
    }

    public int getId() {
        return id;
    }

    public int getSettlement() {
        return settlement;
    }

    public int getPayer() {
        return payer;
    }

    public int getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
