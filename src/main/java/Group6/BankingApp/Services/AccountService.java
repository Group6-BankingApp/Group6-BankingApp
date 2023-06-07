package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.security.auth.login.AccountNotFoundException;
import java.io.Console;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private ModelMapper modelMapper;


    public AccountService() {
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public AccountDTO getAccountByIban(String iban) {
        Optional<Account> accountOptional = accountRepository.findById(iban);
        if (!accountOptional.isPresent())
            throw new ServiceException("Account not found");

        Account account = accountOptional.get();
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

    public AccountDTO addAccount(NewAccountDTO newAccountDTO) {
        try {
            String iban = generateIban();
            Long userId = newAccountDTO.getUserId();
            UserDTO2 userDTO2 = userService.getUserById(userId);
            if (userDTO2 == null)
                throw new ServiceException("User with ID " + userId + " does not exist.");

            String accountType = newAccountDTO.getAccountType();
            String cardUUID = generateCardUUID();
            String pin = newAccountDTO.getPin();
            double dailyLimit = newAccountDTO.getDailyLimit();

            Account account = new Account(iban, userDTO2, accountType, cardUUID, pin, dailyLimit, 0.0, 0.0, true);
            accountRepository.save(account);

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setIban(account.getIban());
            accountDTO.setUser(userDTO2);
            accountDTO.setAccountType(account.getAccountType());
            accountDTO.setCardUUID(account.getCardUUID());
            accountDTO.setPin(account.getPin());
            accountDTO.setDailyLimit(account.getDailyLimit());
            accountDTO.setBalance(account.getBalance());
            accountDTO.setAbsoluteLimit(account.getAbsoluteLimit());

            return accountDTO;
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }

//    public Account updateAccountByIban(String iban,AccountDTO accountDTO) {
//        try {
//            Account accountToUpdate = accountRepository.findById(iban).orElse(null);
//            accountToUpdate.setAccountType(accountDTO.getAccountType());
//            accountToUpdate.setDailyLimit(accountDTO.getDailyLimit());
//            accountToUpdate.setPin(accountDTO.getPin());
//            return accountRepository.save(accountToUpdate);
//        }catch (Exception ex){
//            throw new EntityNotFoundException("Account not found");
//        }
//    }

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

    public String generateCardUUID() {
        // Generate a random UUID for the card
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
