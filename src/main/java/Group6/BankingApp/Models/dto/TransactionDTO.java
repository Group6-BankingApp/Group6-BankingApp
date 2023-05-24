package Group6.BankingApp.Models.dto;

public class TransactionDTO{
    private double amount;
    private String recieverIban;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, String recieverIban) {
        this.amount = amount;
        this.recieverIban = recieverIban;
    }

    public double getAmount() {
        return amount;
    }

    public String getRecieverIban() {
        return recieverIban;
    }
}