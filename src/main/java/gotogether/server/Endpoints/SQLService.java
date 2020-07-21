package gotogether.server.Endpoints;

import gotogether.server.Model.*;
import gotogether.server.Utils.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SQLService {

    private Connector connector;

    @Autowired
    public SQLService() {
        connector = new Connector();
    }


    // ========================================== USERS ============================================
    public List<User> GetAllUsers() {
        List<User> result = User.GetAllUsers(connector.Connect());
        connector.Disconnect();
        return result;
    }

    public User FindUserById(int id) {
        User result = User.FindUserById(id, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public User FindUserByName(String name) {
        User result = User.FindUserByName(name, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean CreateNewUser(User user) {
        boolean result = user.PostThisUser(connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean ChangePassword(String username, String password) {
        boolean result = User.ChangePassword(username, password, connector.Connect());
        connector.Disconnect();
        return result;
    }


    // ======================================== EVENTS =========================================
    public List<Event> GetAllEvents() {
        List<Event> result = Event.GetAllEvents(connector.Connect());
        connector.Disconnect();
        return result;
    }

    public List<Event> GetAllUserEvents(String username) {
        List<Event> result = Event.GetAllUserEvents(username, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public Event FindEventById(int id) {
        Event result = Event.FindEventById(id, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public EventSummary GetEventSummary(int id) {
        EventSummary eventSummary = new EventSummary(Event.FindEventById(id, connector.Connect()),
                Participant.GetAllEventParticipants(id, connector.Connect()),
                Expense.GetAllEventExpenses(id, connector.Connect()));
        connector.Disconnect();
        return eventSummary;
    }

    public EventFinalSummary GetEventFinalSummary(int id) {
        EventFinalSummary eventFinalSummary = new EventFinalSummary(
                Event.FindEventById(id, connector.Connect()),
                Participant.GetAllEventParticipants(id, connector.Connect()),
                Expense.GetAllEventExpenses(id, connector.Connect()),
                Settlement.FindEventSettlement(id, connector.Connect()));
        connector.Disconnect();
        return eventFinalSummary;
    }

    public boolean AddNewEvent(Event event) {
        boolean result = event.PostThisEvent(connector.Connect());
        connector.Disconnect();
        return result;
    }


    // ===================================== PARTICIPANTS =======================================
    public List<Participant> GetAllEventParticipants(int eventId) {
        List<Participant> result = Participant.GetAllEventParticipants(eventId, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public Participant FindParticipantById(int id) {
        Participant result = Participant.FindParticipantById(id, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean AddParticipant(Participant participant) {
        boolean result = participant.AddThisParticipant(connector.Connect());
        connector.Disconnect();
        return result;
    }


    // ====================================== EXPENSES =========================================
    public List<Expense> GetAllEventExpenses(int eventId) {
        List<Expense> result = Expense.GetAllEventExpenses(eventId, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public List<Expense> GetAllPersonalExpenses(int participantId) {
        List<Expense> result = Expense.GetAllPersonalExpenses(participantId, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public Expense FindExpenseById(int id) {
        Expense result = Expense.FindExpenseById(id, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean AddNewExpense(Expense expense) {
        boolean result = expense.AddThisExpense(connector.Connect());
        connector.Disconnect();
        return result;
    }


    // ==================================== SETTLEMENTS ======================================
    public Settlement FindSettlementById(int id) {
        Settlement result = Settlement.FindSettlementById(id, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public Settlement FindEventSettlement(int eventId) {
        Settlement result = Settlement.FindEventSettlement(eventId, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean AddNewSettlement(Settlement settlement) {
        boolean result = settlement.AddThisSettlement(connector.Connect());
        connector.Disconnect();
        return result;
    }


    // =================================== TRANSACTIONS =====================================
    public List<Transaction> GetAllTransactions(int settlementId) {
        List<Transaction> result = Transaction.GetAllTransactions(settlementId, connector.Connect());
        connector.Disconnect();
        return result;
    }

    public boolean AddNewTransaction(Transaction transaction) {
        boolean result = transaction.AddThisTransaction(connector.Connect());
        connector.Disconnect();
        return result;
    }
}
