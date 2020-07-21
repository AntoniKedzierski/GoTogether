package gotogether.server.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import gotogether.server.Endpoints.SQLService;
import gotogether.server.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("db")
public class DBController {

    private final SQLService sqlService;

    @Autowired
    public DBController(SQLService sqlService) {
        this.sqlService = sqlService;
    }

    @GetMapping("/test")
    public String testController() {
        return "The DB Controller works properly!";
    }


    // =================================== USERS ===================================
    @GetMapping("/users/all")
    public List<User> GetAllUsers() { return sqlService.GetAllUsers(); }

    @GetMapping("/users")
    public User FindUser(@RequestParam("name") String name, @RequestParam("id") int id) {
        if (!name.equals("")|| id != 0) return sqlService.FindUserById(id);
        return sqlService.FindUserByName(name);
    }

    @PostMapping("/users")
    public boolean AddNewUser(@RequestBody User user) { return sqlService.CreateNewUser(user); }

    @PutMapping("/users")
    public boolean ChangePassword(@RequestParam("name") String name, @RequestBody String password) {
        return sqlService.ChangePassword(name, password);
    }


    // =================================== EVENTS ==================================
    @GetMapping("/events/all")
    public List<Event> GetAllEvents() { return sqlService.GetAllEvents(); }

    @GetMapping("/events")
    public List<Event> FindEvent(@RequestParam("owner") String username, @RequestParam("id") int id) {
        if (username.equals("")) {
            List<Event> result = new ArrayList<>();
            result.add(sqlService.FindEventById(id));
            return result;
        }
        return sqlService.GetAllUserEvents(username);
    }

    @GetMapping("/events/data")
    public EventSummary GetEventSummary(@RequestParam("id") int id) {
        return sqlService.GetEventSummary(id);
    }

    @GetMapping("events/final")
    public EventFinalSummary GetEventFinalSummary(@RequestParam("id") int id) {
        return sqlService.GetEventFinalSummary(id);
    }

    @PostMapping("/events")
    public boolean AddNewEvent(@RequestBody Event event) { return sqlService.AddNewEvent(event); }


    // ================================ PARTICIPANTS ===============================
    @GetMapping("/events/pars")
    public List<Participant> GetAllEventParticipant(@RequestParam("event") int id) { return sqlService.GetAllEventParticipants(id); }

    @GetMapping("/pars")
    public Participant FindParticipantById(@RequestParam("id") int id) { return sqlService.FindParticipantById(id); }

    @PostMapping("/pars")
    public boolean AddNewParticipant(@RequestBody Participant participant) { return sqlService.AddParticipant(participant); }


    // ================================== EXPENSES =================================
    @GetMapping("/events/exps")
    public List<Expense> GetAllEventExpenses(@RequestParam("event") int id) { return sqlService.GetAllEventExpenses(id); }

    @GetMapping("/pars/exps")
    public List<Expense> GetAllPersonalExpenses(@RequestParam("person") int id) { return sqlService.GetAllPersonalExpenses(id); }

    @GetMapping("/exps")
    public Expense FindExpenseById(@RequestParam("id") int id) { return sqlService.FindExpenseById(id); }

    @PostMapping("/exps")
    public boolean AddNewExpense(@RequestBody Expense expense) { return sqlService.AddNewExpense(expense); }


    // ================================= SETTLEMENTS ================================
    @GetMapping("/events/sett")
    public Settlement GetEventSettlement(@RequestParam("event") int id) { return sqlService.FindEventSettlement(id); }

    @GetMapping("/sett")
    public Settlement FindSettlementById(@RequestParam("id") int id) { return sqlService.FindSettlementById(id); }

    @PostMapping("/sett")
    public boolean AddNewSettlement(@RequestBody Settlement settlement) { return sqlService.AddNewSettlement(settlement); }


    // ================================ TRANSACTIONS ================================
    @GetMapping("/events/tran")
    public List<Transaction> GetAllTransaction(@RequestParam("settlement") int id) { return sqlService.GetAllTransactions(id); }

    @PostMapping("/tran")
    public boolean AddNewTransaction(@RequestBody Transaction transaction) { return sqlService.AddNewTransaction(transaction); }
}
