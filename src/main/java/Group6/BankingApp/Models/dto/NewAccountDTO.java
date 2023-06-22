package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;

public class NewAccountDTO {

    private Long userId;
    private String accountType;
    private String pin;
    private double dailyLimit;
    private double balance;
    private double absoluteLimit;
    private double transactionLimit;

    public NewAccountDTO() {
    }

    public NewAccountDTO(Account account) {
        this.userId = account.getUser().getId();
        this.accountType = account.getAccountType();
        this.pin = account.getPin();
        this.dailyLimit = account.getDailyLimit();
        this.balance = account.getBalance();
        this.absoluteLimit = account.getAbsoluteLimit();
        this.transactionLimit = account.getTransactionLimit();
    }

//    public NewAccountDTO( String accountType, String pin) {
//        this.accountType = accountType;
//        this.pin = pin;
//        this.dailyLimit = 2000.00;
//    }
//
//    public NewAccountDTO( Long userId,String accountType, String pin, double dailyLimit, double balance, double absoluteLimit,double transactionLimit) {
//        this.userId = userId;
//        this.accountType = accountType;
//        this.pin = pin;
//        this.dailyLimit = dailyLimit;
//        this.balance = balance;
//        this.absoluteLimit = absoluteLimit;
//        this.transactionLimit = transactionLimit;
//    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public double getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(double transactionLimit) {
        this.transactionLimit = transactionLimit;
    }
}
