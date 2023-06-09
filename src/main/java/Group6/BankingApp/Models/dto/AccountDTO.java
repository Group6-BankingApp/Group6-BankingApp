package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
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

    public AccountDTO(){}

    public AccountDTO(Account account){
        this.iban = account.getIban();
        this.user = new UserDTO2(account.getUser());
        this.accountType = account.getAccountType();
        this.cardUUID = account.getCardUUID();
        this.pin = account.getPin();
        this.dailyLimit = account.getDailyLimit();
        this.balance = account.getBalance();
        this.absoluteLimit = account.getAbsoluteLimit();
    }
    public AccountDTO(String iban, UserDTO2 user, String accountType, String cardUUID, String pin, double dailyLimit, double balance, double absoluteLimit) {
        this.iban = iban;
        this.user = user;
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
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
        this.dailyLimit = dailyLimit;
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
}
