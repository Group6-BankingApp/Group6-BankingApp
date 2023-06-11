package Group6.BankingApp.Models;
import java.util.UUID;
import java.time.LocalDate;
import Group6.BankingApp.Models.dto.TransactionDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transactionId", updatable = false, nullable = false)
    private String transactionId;
    private LocalDate timeCreated;
    private String senderIban;
    private String receiverIban;
    private double amount;
    private String description;

    public Transaction() {
    }

    public Transaction(String senderIban, String receiverIban, double amount, String description) {
        this.timeCreated = LocalDate.now();
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.description = description;
    }

    //TODO add userId from session token
    public Transaction(TransactionDTO transactionDTO) {
        this.timeCreated = LocalDate.now();
        this.senderIban = transactionDTO.getSenderIban();
        this.receiverIban = transactionDTO.getReceiverIban();
        this.amount = transactionDTO.getAmount();
        this.description = transactionDTO.getDescription();
    }


    public String getTransactionId() {
        return transactionId;
    }
    public  void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public LocalDate getTimeCreated() {
        return timeCreated;
    }

    public  void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public  void setreceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public  void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public Double getAmount() {
        return amount;
    }

    public  void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }
    
    public  void setDescription(String description) {
        this.description = description;
    }
}
