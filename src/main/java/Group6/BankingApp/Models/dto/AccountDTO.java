package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDTO {

    private String iban;
    private String accountType;
    private String pin;
    private double dailyLimit;
    private double balance;
    private double absoluteLimit;
    private double transactionLimit;
    private String hasCard;
    private String cardNumber;
    public String cardPublicNumber;

    public AccountDTO() {
    }

    public AccountDTO(Account account){
        this.iban = account.getIban();
        this.accountType = account.getAccountType();
        this.pin = account.getPin();
        this.dailyLimit = account.getDailyLimit();
        this.balance = account.getBalance();
        this.absoluteLimit = account.getAbsoluteLimit();
        this.transactionLimit = account.getTransactionLimit();
        if(account.getHasCard()){
            this.hasCard = "Yes";
            this.cardNumber = account.getCardNumber();
            this.cardPublicNumber = String.valueOf(account.getCardPublicNumber());
        }   else {
            this.hasCard = "No";
            this.cardNumber = "N/A";
            this.cardPublicNumber = "N/A";
        }
    }


    public AccountDTO(String iban, String accountType, String pin, double dailyLimit, double balance, double absoluteLimit,double transactionLimit, Boolean hasCard) {
        this.iban = iban;
        this.accountType = accountType;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.transactionLimit = transactionLimit;
        if (hasCard) {
            this.hasCard = "Yes";
        } else {
            this.hasCard = "No";
        }
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    public String getHasCard() {
        return hasCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPublicNumber() {
        return cardPublicNumber;
    }

}
