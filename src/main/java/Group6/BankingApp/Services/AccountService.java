package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService() {
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account getAccountByIban(String iban) {
        return accountRepository.findById(iban).orElse(null);
    }

    public Account addAccount(AccountDTO accountDTO)
    {
        return accountRepository.save(new Account(accountDTO.getIban(), accountDTO.getUser(), "current", accountDTO.getCardUUID(), accountDTO.getPin(), accountDTO.getDailyLimit(), 100.00, 500.00, true));
    }

    public Account updateAccountByIban(String iban,AccountDTO accountDTO) {
        try {
            Account accountToUpdate = accountRepository.findById(iban).orElse(null);
            accountToUpdate.setAccountType(accountDTO.getAccountType());
            accountToUpdate.setDailyLimit(accountDTO.getDailyLimit());
            accountToUpdate.setPin(accountDTO.getPin());
            return accountRepository.save(accountToUpdate);
        }catch (Exception ex){
            throw new EntityNotFoundException("Account not found");
        }
    }

    public List<AccountDTO> getAccountsWithSkipAndLimit(Integer skip, Integer limit){
        Pageable pageable = PageRequest.of(skip, limit);
        return accountRepository.findAllBy(pageable);
    }

    //findAllByOrderByCreatedDateDesc

    public void deactivateAccount(String iban, DebitCardDTO debitCardDTO) {
        Account account = accountRepository.findByIban(iban);
        if (account != null && account.getDebitCard() != null && account.getDebitCard().getCardNumber().equals(debitCardDTO.getCardNumber())) {
            account.setStatus(false);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Invalid account or debit card");
        }
    }

    public void deactivateDebitCard(String iban, DebitCardDTO debitCardDTO) {
        Account account = accountRepository.findByIban(iban);

        if (account != null && account.getDebitCard() != null && account.getDebitCard().getCardNumber().equals(debitCardDTO.getCardNumber())) {
            DebitCard debitCard = account.getDebitCard();
            debitCard.setActive(false);
            account.setDebitCard(debitCard);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Invalid account or debit card");
        }
    }

    public double getAccountBalance(String iban, String pin) {
        Account account = accountRepository.findByIban(iban);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new EntityNotFoundException("Invalid account or pin");
        }
    }

    public void deleteAccount(String iban) {
        accountRepository.deleteById(iban);
    }
}
