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
    private Long userId;


    public Transaction() {
    }

    public Transaction(String senderIban, String receiverIban, double amount) {
        this.timeCreated = LocalDate.now();
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
    }

    public Transaction(TransactionDTO transactionDTO) {
        this.timeCreated = LocalDate.now();
        this.senderIban = transactionDTO.getSenderIban();
        this.receiverIban = transactionDTO.getReceiverIban();
        this.amount = transactionDTO.getAmount();
        this.userId = transactionDTO.getUserDTO2().getId();
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

    public  void setReceiverIban(String receiverIban) {
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
}
