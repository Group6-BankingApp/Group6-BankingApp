package Group6.BankingApp.Models;

public class DepositDTO {
    private double amount;

    public DepositDTO() {
    }

    public DepositDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
