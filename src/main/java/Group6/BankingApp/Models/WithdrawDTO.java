package Group6.BankingApp.Models;

public class WithdrawDTO {

    private double amount;

    public WithdrawDTO() {
    }

    public WithdrawDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
