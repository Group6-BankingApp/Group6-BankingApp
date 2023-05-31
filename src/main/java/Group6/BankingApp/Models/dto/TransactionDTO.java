package Group6.BankingApp.Models.dto;

public class TransactionDTO{
    private double amount;
    private String recieverIban;
    private String senderIban;
    private String description;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, String recieverIban, String senderIban, String description) {
        this.amount = amount;
        this.recieverIban = recieverIban;
        this.senderIban = senderIban;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getRecieverIban() {
        return recieverIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public String getDescription() {
        return description;
    }
}