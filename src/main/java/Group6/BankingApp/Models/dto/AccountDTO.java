package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import jakarta.persistence.OneToOne;

public class AccountDTO {

    private String iban;
    private UserDTO2 user;
    private String accountType;
    private String cardUUID;
    private String pin;
    private double dailyLimit;

    public AccountDTO(){}

    public AccountDTO(String iban, UserDTO2 user, String accountType, String cardUUID, String pin, double dailyLimit) {
        this.iban = iban;
        this.user = user;
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
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
}
