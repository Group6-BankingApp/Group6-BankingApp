package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.DebitCard;

public class DebitCardDTO2 {

    private String cardNumber;
    private Integer publicNumber;
    private String expirationDate;
    private boolean isActive;

    public DebitCardDTO2(String cardNumber, String expirationDate, boolean isActive) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public DebitCardDTO2(DebitCard debitCard) {
        this.cardNumber = debitCard.getCardNumber();
        this.publicNumber = debitCard.getPublicNumber();
        this.expirationDate = debitCard.getExpirationDate().toString();
        this.isActive = debitCard.isActive();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Integer getPublicNumber() {
        return publicNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public boolean isActive() {
        return isActive;
    }
}
