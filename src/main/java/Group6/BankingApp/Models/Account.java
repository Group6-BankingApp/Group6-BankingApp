package Group6.BankingApp.Models;

import Group6.BankingApp.Models.dto.UserDTO2;
import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(unique = true)
    private String iban;
    @OneToOne
    private User user;
    private String accountType;
    private String pin;
    private double dailyLimit;
    private double balance;
    private double absoluteLimit;
    private double transactionLimit;
    private boolean hasCard;
    private String cardNumber;


    public Account(String iban, String accountType, String pin, double dailyLimit, double balance, double absoluteLimit, double transactionLimit) {
        this.iban = iban;
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.transactionLimit = transactionLimit;
    }

    public Account(String iban, User user, String accountType, String pin, double dailyLimit, double balance, double absoluteLimit, double transactionLimit) {
        this.iban = iban;
        this.user = user;
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.transactionLimit = transactionLimit;
    }

    public Account(String iban, UserDTO2 userDTO2, String accountType, String pin, double dailyLimit, double balance, double absoluteLimit) {
        this.iban = iban;
        this.user = mapToUser(userDTO2);
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
    }

    public Account() {
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(double absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public boolean getHasCard() {
        if (hasCard) {
            return true;
        } else {
            return false;
        }
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }

    public double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    private User mapToUser(UserDTO2 userDTO2) {
        User user = new User();
        user.setId(userDTO2.getId());
        user.setFirstName(userDTO2.getFirstName());
        user.setLastName(userDTO2.getLastName());
        user.setEmail(userDTO2.getEmail());
        user.setPhoneNumber(userDTO2.getPhoneNumber());
        return user;
    }
}
