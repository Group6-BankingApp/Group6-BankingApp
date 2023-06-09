package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.UserService;
import jakarta.persistence.OneToOne;
import org.springframework.stereotype.Component;
import org.hibernate.service.spi.ServiceException;


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

    public AccountDTO(NewAccountDTO newAccountDTO, UserDTO2 usr){
        this.iban = AccountService.generateIban();
        this.user = usr;
        this.accountType = newAccountDTO.getAccountType();
        this.cardUUID = AccountService.generateCardUUID();
        this.pin = newAccountDTO.getPin();
        this.dailyLimit = newAccountDTO.getDailyLimit();
    }

    public AccountDTO(Account account)
    {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        User user = account.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Perform similar null checks for other required fields

        this.iban = account.getIban();
        this.user = new UserDTO2(user);
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
