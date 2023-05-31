package Group6.BankingApp.Models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private Long userId;
    private LocalDate timeCreated;
    private String senderIban;
    private String recieverIban;
    private double amount;

    public Transaction() {
    }

    public Transaction(LocalDate timeCreated, String senderIban, String recieverIban, double amount) {
        this.timeCreated = timeCreated;
        this.senderIban = senderIban;
        this.recieverIban = recieverIban;
        this.amount = amount;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }


    public Long getTransactionId() {
        return transactionId;
    }

    public Long getUserId() {
        return userId;
    }

    public  void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getTimeCreated() {
        return timeCreated;
    }

    public  void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getRecieverIban() {
        return recieverIban;
    }

    public  void setRecieverIban(String recieverIban) {
        this.recieverIban = recieverIban;
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
