package gotogether.server.Model;

import java.util.List;

public class EventSummary {

    private Event event;
    private List<Participant> participants;
    private List<Expense> expenses;
    private float totalCost;
    private float averageCost;
    private int numPeople;

    public EventSummary(Event event, List<Participant> participants, List<Expense> expenses) {
        this.event = event;
        this.participants = participants;
        this.expenses = expenses;
        this.numPeople = participants.size();
        this.totalCost = 0;
        for (Expense e : expenses) {
            this.totalCost += e.getValue();
        }
        this.totalCost = Math.round(this.totalCost * 100) / 100.0f;
        this.averageCost = Math.round(this.totalCost / this.numPeople * 100) / 100.0f;
    }

    public Event getEvent() {
        return event;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public float getAverageCost() {
        return averageCost;
    }

    public int getNumPeople() {
        return numPeople;
    }
}
