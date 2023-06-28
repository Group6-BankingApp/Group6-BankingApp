package Group6.BankingApp.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "debit_cards")
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cardNumber;
    private Integer publicNumber;
    private LocalDate expirationDate;
    private boolean isActive;
    private String uuid;

    @OneToOne
    private Account account;

    public DebitCard() {
    }

    public DebitCard(String cardNumber, Integer publicNumber, LocalDate expirationDate, boolean isActive, String uuid, Account account) {
        this.cardNumber = cardNumber;
        this.publicNumber = publicNumber;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.uuid = uuid;
        this.account = account;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getPublicNumber() {
        return publicNumber;
    }

    public void setPublicNumber(Integer publicNumber) {
        this.publicNumber = publicNumber;
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
