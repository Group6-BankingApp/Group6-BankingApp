package Group6.BankingApp.Models.dto;

public class DebitCardDTO {
    private String debitCardNumber;

    public DebitCardDTO() {
    }

    public DebitCardDTO(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }

    public String getCardNumber() {
        return debitCardNumber;
    }

    public void setCardNumber(String debitCardNumber) {
        this.debitCardNumber = debitCardNumber;
    }
}
