package Group6.BankingApp.Models.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
public class TransactionDTO{
    private double amount;
    private String receiverIban;
    private String senderIban;
    private String description;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, String receiverIban, String senderIban, String description) {
        this.amount = amount;
        this.receiverIban = receiverIban;
        this.senderIban = senderIban;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public String getDescription() {
        return description;
    }
}