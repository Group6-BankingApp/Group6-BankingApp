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
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        try {
            Account account = new Account(
                    accountDTO.getIban(),
                    accountDTO.getUser(),
                    accountDTO.getAccountType(),
                    accountDTO.getCardUUID(),
                    accountDTO.getPin(),
                    accountDTO.getDailyLimit(),
                    0.0,
                    0.0,
                    true,
                    null
            );
            return accountRepository.save(account);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
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

//    public List<AccountDTO> getAccountsWithSkipAndLimit(Integer skip, Integer limit){
//        try {
//            List<Account> accounts = accountRepository.findAllBy(PageRequest.of(1, limit));
//
//            List<AccountDTO> DTOs = new ArrayList<>();
//
//            for (Account account : accounts) {
//                DTOs.add(new AccountDTO(account));
//            }
//
//            if(accounts.size() > skip){
//                return DTOs.subList(skip, Math.min(skip+limit, DTOs.size()));
//            } else
//                return Collections.emptyList();
//        }catch (Exception ex){
//            throw new ServiceException("Failed to retrieve accounts", ex);
//        }
//    }

//    public List<AccountDTO> findAllAccounts(Pageable pageable) {
//        List<Account> accounts = accountRepository.findAllBy(pageable);
//        return accounts.stream()
//                .map(this::mapToAccountDTO)
//                .collect(Collectors.toList());
//    }

    public List<AccountDTO> findAllAccounts(Integer skip, Integer limit) {
        try {
            List<Account> accounts = accountRepository.findAllBy(PageRequest.of(skip, limit));
            if(accounts == null)
                throw new NullPointerException("Accounts list is null");

            return accounts.stream()
                    .map(this::mapToAccountDTO)
                    .collect(Collectors.toList());
        }catch (Exception ex){
            throw new ServiceException("Failed to retrieve accounts", ex);
        }
    }

    private AccountDTO mapToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban(account.getIban());
        accountDTO.setUser(account.getUser());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setCardUUID(account.getCardUUID());
        accountDTO.setPin(account.getPin());
        accountDTO.setDailyLimit(account.getDailyLimit());
        return accountDTO;
    }

    public void deactivateAccount(String iban, DebitCardDTO debitCardDTO) {
        Account account = accountRepository.findByIban(iban);
        if (account != null && account.getDebitCard() != null && account.getDebitCard().getCardNumber().equals(debitCardDTO.getCardNumber())) {
            account.setStatus(false);
            accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Invalid account or debit card");
        }
    }

    public void deactivateDebitCard(String iban, DebitCardDTO debitCardDTO){
        try {
            Account account = accountRepository.findByIban(iban);
            if(account == null)
                throw new ServiceException("Invalid IBAN");

            DebitCard debitCard = account.getDebitCard();
            if (debitCard == null && debitCard.getCardNumber() != debitCardDTO.getCardNumber())
                throw new ServiceException("Invalid debit card details");
            debitCard.setActive(false);
            accountRepository.save(account);
        }catch (Exception ex){
            throw new ServiceException("Failed to deactivate debit card", ex);
        }
    }

    public double getAccountBalance(String iban, String pin) {
        try{
            Account account = accountRepository.findByIban(iban);
            if (account == null && account.getPin() != pin)
                throw new EntityNotFoundException("Invalid account or pin");
            return account.getBalance();
        }catch (Exception ex){
            throw new ServiceException("Failed to retrieve account balance", ex);
        }
    }

    public void deleteAccount(String iban) {
        accountRepository.deleteById(iban);
    }
}
