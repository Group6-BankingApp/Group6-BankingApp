package Group6.BankingApp.Models.dto;

public class AtmTransactionDTO {

    private String iban;
    private String amount;

    public AtmTransactionDTO() {
    }

    public AtmTransactionDTO(String iban, String amount) {
        this.iban = iban;
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
