package Group6.BankingApp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    //private AccountRepository accountRepository;

    public Deposit() {
    }

    public Deposit(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private boolean validatePin(String pin) {
        if (pin == null || pin.length() != 4) {
            return false; // PIN length is not 4 digits
        }
        // uncomment when reppository classes added
        // String storedPin = accountRepository.getPin();
        // return pin.equals(storedPin);

        //delete line after
        return true;
    }
    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        // uncomment when reppository classes added
        // String storedCardNumber = accountRepository.getCardNumber();
        // return cardNumber.equals(storedCardNumber);

        //delete line after
        return true;
    }

    public void performDeposit(double amount, String pin, String cardNumber, String uuid) {


    }

}
