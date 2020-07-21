package gotogether.server.Model;

import java.util.ArrayList;
import java.util.List;

public class EventFinalSummary {

    private Event event;
    private List<Participant> participants;
    private List<Expense> expenses;
    private List<Transaction> transactions;
    private float totalCost;
    private float averageCost;
    private int numPeople;

    public EventFinalSummary(Event event, List<Participant> participants, List<Expense> expenses, Settlement settlement) {
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

        // System.out.println("Total cost: " + this.totalCost);
        // System.out.println("Average cost: " + this.averageCost);

        // Make a final settlement
        List<Participant> bulls = new ArrayList<>();
        List<Participant> bears = new ArrayList<>();

        for (Participant p : participants) {
            float totalExpenses = 0.0f;
            for (Expense e : expenses) {
                if (e.getPayer() == p.getId()) totalExpenses += e.getValue();
            }
            p.setDebt(Math.abs(Math.round((this.averageCost - totalExpenses) * 100) / 100.0f));
            if (totalExpenses > this.averageCost) bulls.add(p);
            if (totalExpenses < this.averageCost) bears.add(p);
            // System.out.println(p.getName() + " paid " + totalExpenses);

        }

        this.transactions = new ArrayList<>();
        for (Participant bull : bulls) {
            for (Participant bear : bears) {
                if (bear.getDebt() > 0.0f) {
                    float value = Math.min(bear.getDebt(), bull.getDebt());
                    // System.out.println(bull.getName() + " (" + bull.getDebt() + ") would be paid by " + bear.getName() + " (" + bear.getDebt() + ") with " + value);
                    bull.setDebt(bull.getDebt() - value);
                    bear.setDebt(bear.getDebt() - value);
                    this.transactions.add(new Transaction(
                            settlement.getId(),
                            bear.getId(),
                            bull.getId(),
                            value,
                            "COMING SOON"
                    ));
                }
            }
        }

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

    public List<Transaction> getTransactions() {
        return transactions;
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
