package Group6.BankingApp.Models.dto;

import jakarta.persistence.OneToOne;

public class AccountDTO {

    private String iban;
    private User user;
    private String cardUUID;
    private String pin;
    private double dailyLimit;

    public AccountDTO(String iban, User user, String cardUUID, String pin, double dailyLimit) {
        this.iban = iban;
        this.user = user;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
    }

    public AccountDTO() {
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
