package Group6.BankingApp.Models.dto;

import org.springframework.stereotype.Component;

@Component
public class AccountDTO {

    private String iban;
    private UserDTO2 user;
    private String accountType;
    private String cardUUID;
    private String pin;
    private double dailyLimit;
    private double balance;
    private double absoluteLimit;
    private double transactionLimit;
    private String cardNumber;

    public AccountDTO(){}

    public AccountDTO(String iban, UserDTO2 user, String accountType, String cardUUID, String pin, double dailyLimit, double balance, double absoluteLimit,double transactionLimit, String cardNumber) {
        this.iban = iban;
        this.user = user;
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.transactionLimit = transactionLimit;
        this.cardNumber = cardNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public UserDTO2 getUser() {
        return user;
    }

    public void setUser(UserDTO2 user) {
        this.user = user;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCardUUID() {
        return cardUUID;
    }

    public void setCardUUID(String cardUUID) {
        this.cardUUID = cardUUID;
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
        if(dailyLimit < 0)
            throw new IllegalArgumentException("Daily limit cannot be negative");
        this.dailyLimit = dailyLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if(balance < 0)
            throw new IllegalArgumentException("Balance limit cannot be negative");
        this.balance = balance;
    }

    public double getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(double absoluteLimit) {
        if(absoluteLimit < 0)
            throw new IllegalArgumentException("Absolute limit cannot be negative");
        this.absoluteLimit = absoluteLimit;
    }

    public double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(double transactionLimit) {
        if(transactionLimit < 0)
            throw new IllegalArgumentException("Transaction limit cannot be negative");
        this.transactionLimit = transactionLimit;
    }

    public String getDebitCardNumber() {
        return cardNumber;
    }

    public void setDebitCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
