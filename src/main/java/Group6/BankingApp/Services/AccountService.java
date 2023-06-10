package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public AccountService() {}

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public AccountDTO getAccountByIban(String iban) {
        Optional<Account> accountOptional = accountRepository.findById(iban);
        if (!accountOptional.isPresent())
            throw new ServiceException("Account not found");

        Account account = accountOptional.get();
        return new AccountDTO(account);
    }

    public AccountDTO addAccount(NewAccountDTO newAccountDTO) {
        try {
            String iban = generateIban();
            Long userId = newAccountDTO.getUserId();

            User user = userService.getFullUserById(userId);
            UserDTO2 userDTO2 = new UserDTO2(user);

            user.setHasAccount(true);
            userRepository.save(user);

            String accountType = newAccountDTO.getAccountType();
            String cardUUID = generateCardUUID();
            String pin = newAccountDTO.getPin();
            double dailyLimit = newAccountDTO.getDailyLimit();

            Account account = new Account(iban, userDTO2, accountType, cardUUID, pin, dailyLimit, 0.0, 0.0, true);
            accountRepository.save(account);

            return new AccountDTO(account);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }

    public NewAccountDTO updateAccountByIban(String iban, AccountDTO accountDTO) {
        Account account = accountRepository.findById(iban)
                .orElse(null);

        Long userId = accountDTO.getUser().getId();
        User user = userRepository.findById(userId)
                .orElse(null);

        // Update the account from accountDTO
        account.setIban(accountDTO.getIban());
        account.setUser(user);
        account.setAccountType(accountDTO.getAccountType());
        account.setCardUUID(accountDTO.getCardUUID());
        account.setPin(accountDTO.getPin());
        account.setDailyLimit(accountDTO.getDailyLimit());
        account.setBalance(accountDTO.getBalance());
        account.setAbsoluteLimit(accountDTO.getAbsoluteLimit());

        Account updatedAccount = accountRepository.save(account);
        return new NewAccountDTO(updatedAccount);
    }

    public List<AccountDTO> findAllAccounts(Integer skip, Integer limit) {
        try {
            Iterable<Account> allAccounts = accountRepository.findAll();
            if (allAccounts == null)
                throw new ServiceException("Failed to retrieve accounts");

            List<Account> accountList = new ArrayList<>();
            allAccounts.forEach(accountList::add);

            int totalAccounts = accountList.size();

            List<AccountDTO> accountDTOs = new ArrayList<>();
            if(skip >= totalAccounts)
                return accountDTOs;

            int end = Math.min(skip + limit, totalAccounts);
            for (int i = skip; i < end; i++) {
                Account account = accountList.get(i);
                AccountDTO accountDTO = mapToAccountDTO(account);
                accountDTOs.add(accountDTO);
            }
            return accountDTOs;
        }catch (Exception ex){
            throw new ServiceException("Failed to retrieve accounts", ex);
        }
    }

    public void editDebitCard(String iban, DebitCardDTO debitCardDTO, boolean active){
        try {
            Account account = accountRepository.findByIban(iban);
            if(account == null)
                throw new ServiceException("Invalid IBAN");

            DebitCard debitCard = account.getDebitCard();
            if (debitCard == null && debitCard.getCardNumber() != debitCardDTO.getCardNumber())
                throw new ServiceException("Invalid debit card details");
            debitCard.setActive(active);
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

    public NewAccountDTO updatePin(String iban, AccountDTO accountDTO) {
        Account account = accountRepository.findById(iban)
                .orElseThrow(null);

        // Update the account's pin
        account.setPin(accountDTO.getPin());

        Account updatedAccount = accountRepository.save(account);
        return new NewAccountDTO(updatedAccount);
    }

    public void deleteAccount(String iban) {
        accountRepository.deleteById(iban);
    }

    protected AccountDTO mapToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban(account.getIban());
        accountDTO.setUser(mapToUserDTO2(account.getUser()));
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setCardUUID(account.getCardUUID());
        accountDTO.setPin(account.getPin());
        accountDTO.setDailyLimit(account.getDailyLimit());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setAbsoluteLimit(account.getAbsoluteLimit());

        return accountDTO;
    }

    private UserDTO2 mapToUserDTO2(User user) {
        UserDTO2 userDTO = new UserDTO2();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());

        return userDTO;
    }

    private static final String IBAN_PREFIX = "NL01INHO";

    public static String generateIban() {
        StringBuilder ibanBuilder = new StringBuilder(IBAN_PREFIX);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10);
            ibanBuilder.append(randomDigit);
        }
        return ibanBuilder.toString();
    }

    public static String generateCardUUID() {
        // Generate a random UUID for the card
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}