package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService() {
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account getAccountById(String iban) {
        return accountRepository.findById(iban).orElse(null);
    }

    public void addAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccount(Account account) {
        try {
            accountRepository.save(account);
        }catch (Exception ex){
            throw new EntityNotFoundException("Account not found");
        }
    }

    public void deleteAccount(String iban) {
        accountRepository.deleteById(iban);
    }
}
