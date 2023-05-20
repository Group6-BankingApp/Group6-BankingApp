package Group6.BankingApp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    private String iban;

    @ManyToOne
    private User user;
    private String status;
    private String accountType;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private BigDecimal minimumBalance;

    @OneToOne
    private AccountPin pin;

    @OneToOne
    private DebitCard debitCard;

    public Account() {
    }

    public Account(String iban, User user, String status, String accountType, BigDecimal balance, BigDecimal absoluteLimit, BigDecimal minimumBalance, AccountPin pin, DebitCard debitCard) {
        this.iban = iban;
        this.user = user;
        this.status = status;
        this.accountType = accountType;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.minimumBalance = minimumBalance;
        this.pin = pin;
        this.debitCard = debitCard;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public AccountPin getPin() {
        return pin;
    }

    public void setPin(AccountPin pin) {
        this.pin = pin;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }
}
