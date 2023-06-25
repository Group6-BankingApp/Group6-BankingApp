package Group6.BankingApp.Models.dto;

public class AtmResponseDTO {

    private String username;
    private String cardNumber;
    private String iban;
    private String balance;

    public AtmResponseDTO() {
    }

    public AtmResponseDTO(String username, String cardNumber, String iban, String balance) {
        this.username = username;
        this.cardNumber = cardNumber;
        this.iban = iban;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
