package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;

public class NewAccountDTO {

    private Long userId;
    private String accountType;
    private String pin;
    private double dailyLimit;

    public NewAccountDTO() {
    }

    public NewAccountDTO(Long userId, String accountType, String pin) {
        this.userId = userId;
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = 2000.00;
    }

    public NewAccountDTO(Long userId, String accountType, String pin, double dailyLimit) {
        this.userId = userId;
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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
