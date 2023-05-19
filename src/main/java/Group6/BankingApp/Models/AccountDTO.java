package Group6.BankingApp.Models;

public class AccountDTO {

    private String iban;
    private String firstName;
    private String lastName;
    private String cardUUID;
    private String pin;
    private double dailyLimit;

    public AccountDTO(String iban, String firstName, String lastName, String cardUUID, String pin, double dailyLimit) {
        this.iban = iban;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
