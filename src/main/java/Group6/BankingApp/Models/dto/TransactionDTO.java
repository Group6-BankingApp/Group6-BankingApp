package Group6.BankingApp.Models.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
public class TransactionDTO{
    private double amount;
    private String receiverIban;
    private String senderIban;
    private String timeCreated;
    private UserDTO2 userDTO2;

    public TransactionDTO() {
    }

    public TransactionDTO(double amount, String receiverIban, String senderIban, String timeCreated, UserDTO2 userDTO2, String pin) {
        this.amount = amount;
        this.receiverIban = receiverIban;
        this.senderIban = senderIban;
        this.timeCreated = timeCreated;
        this.userDTO2 = userDTO2;
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

    public String getTimeCreated() {
        return timeCreated;
    }

    public UserDTO2 getUserDTO2() {
        return userDTO2;
    }
}