package Group6.BankingApp.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String debitCardNumber;
    private LocalDate expirationDate;
    private boolean isActive;
    private String uuid;

    @OneToOne
    private Account account;

    public DebitCard() {
    }

    public DebitCard(String debitCardNumber,LocalDate expirationDate, boolean isActive, String uuid) {
        this.debitCardNumber = debitCardNumber;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return debitCardNumber;
    }

    public void setCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
