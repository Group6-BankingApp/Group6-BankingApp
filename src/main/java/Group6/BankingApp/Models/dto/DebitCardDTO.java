package Group6.BankingApp.Models.dto;

public class DebitCardDTO {
    private String cardNumber;
    private String uuid;
    private String expirationDate;
    private boolean isActive;

    public DebitCardDTO(String cardNumber, String uuid, String expirationDate, boolean isActive) {
        this.cardNumber = cardNumber;
        this.uuid = uuid;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
