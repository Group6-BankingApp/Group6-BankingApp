package Group6.BankingApp.Models;

import Group6.BankingApp.Models.dto.UserDTO2;
import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @Column(unique = true)
    private String iban;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String accountType;
    private String cardUUID;
    private String pin;
    private double dailyLimit;
    private double balance;
    private double absoluteLimit;
    private boolean status;
    private String debitCardNumber;
    @OneToOne
    private DebitCard debitCard;

    public Account(String iban, String accountType, String cardUUID, String pin, double dailyLimit, double balance, double absoluteLimit, boolean status, String debitCardNumber) {
        this.iban = iban;
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.status = status;
        this.debitCardNumber = debitCardNumber;
    }

    public Account(String iban, UserDTO2 userDTO2, String accountType, String cardUUID, String pin, double dailyLimit, double balance, double absoluteLimit, boolean status, String debitCardNumber) {
        this.iban = iban;
        this.user = mapToUser(userDTO2);
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.status = status;
        this.debitCardNumber = debitCardNumber;
    }

    public Account(String iban, UserDTO2 userDTO2, String accountType, String cardUUID, String pin, double dailyLimit, double balance, double absoluteLimit, boolean status) {
        this.iban = iban;
        this.user = mapToUser(userDTO2);
        this.accountType = accountType;
        this.cardUUID = cardUUID;
        this.pin = pin;
        this.dailyLimit = dailyLimit;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.status = status;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    public void setDebitCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
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
