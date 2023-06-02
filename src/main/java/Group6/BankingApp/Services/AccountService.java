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
import java.util.Random;
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
                    generateIban(),
                    //accountDTO.getIban(),
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

//    public List<AccountDTO> findAllAccounts(Integer skip, Integer limit) {
//        try {
//            List<Account> accounts = accountRepository.findAllBy(PageRequest.of(skip, limit));
//            if(accounts == null)
//                throw new NullPointerException("Accounts list is null");
//
//            return accounts.stream()
//                    .map(this::mapToAccountDTO)
//                    .collect(Collectors.toList());
//        }catch (Exception ex){
//            throw new ServiceException("Failed to retrieve accounts", ex);
//        }
//    }

    //SQL query
//    public List<AccountDTO> findAllAccounts(Integer skip, Integer limit){
//        try {
//            List<Account> accounts = accountRepository.findAllAccounts(skip, limit);
//            if (accounts == null)
//                throw new NullPointerException("Accounts list is null");
//
//            return accounts.stream()
//                    .map(this::mapToAccountDTO)
//                    .collect(Collectors.toList());
//        }catch (Exception ex){
//            throw new ServiceException("Failed to retrieve accounts", ex);
//        }
//    }

    public List<AccountDTO> findAllAccounts(Integer skip, Integer limit) {
        Iterable<Account> allAccounts = accountRepository.findAll();
        List<Account> accountList = new ArrayList<>();
        allAccounts.forEach(accountList::add);

        int totalAccounts = accountList.size();

        List<AccountDTO> accountDTOs = new ArrayList<>();

        for (int i = skip; i < Math.min(skip + limit, totalAccounts); i++) {
            Account account = accountList.get(i);
            AccountDTO accountDTO = mapToAccountDTO(account);
            accountDTOs.add(accountDTO);
        }

        return accountDTOs;
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

    private static final String IBAN_PREFIX = "NL01INHO";

    private String generateIban() {
        StringBuilder ibanBuilder = new StringBuilder(IBAN_PREFIX);

        // Generate random digits for the remaining part of the IBAN
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10);
            ibanBuilder.append(randomDigit);
        }

        return ibanBuilder.toString();
    }
}