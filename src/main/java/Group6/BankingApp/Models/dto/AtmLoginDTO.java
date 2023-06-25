package Group6.BankingApp.Models.dto;

public class AtmLoginDTO {

    private String cardUUID;
    private String pin;

    public AtmLoginDTO() {
    }

    public AtmLoginDTO(String cardUUID, String pin) {
        this.cardUUID = cardUUID;
        this.pin = pin;
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
}
