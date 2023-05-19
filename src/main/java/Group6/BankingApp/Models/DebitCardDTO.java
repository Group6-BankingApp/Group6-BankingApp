package Group6.BankingApp.Models;

public class DebitCardDTO {
    private String cardNumber;

    public DebitCardDTO() {
    }

    public DebitCardDTO(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
