package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
public class SavingsAccountDTO {

    private String iban;
    private String accountType;
    private double balance;

    public SavingsAccountDTO() {
    }

    public SavingsAccountDTO(Account account) {
        this.iban = account.getIban();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
