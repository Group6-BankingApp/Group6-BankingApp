package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
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

import java.time.LocalDate;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    private DebitCardRepository debitCardRepository;


    @Autowired
    public AccountService(DebitCardRepository debitCardRepository) {
        this.debitCardRepository = debitCardRepository;
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public AccountDTO getAccountByIban(String iban) {
        Optional<Account> accountOptional = accountRepository.findById(iban);
        if (!accountOptional.isPresent())
            throw new ServiceException("Account not found");

        Account account = accountOptional.get();
        AccountDTO accountDTO = mapToAccountDTO(account);
        return accountDTO;
    }

    public Account findAccountByIban(String iban){
        Account account = accountRepository.findByIban(iban);
        if(account == null)
            throw new ServiceException("This account does not exist.");
        return account;
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

            Account account = new Account(iban, userDTO2, accountType, cardUUID, pin, dailyLimit, 0.0, 0.0, true, null);
            accountRepository.save(account);

            AccountDTO accountDTO = mapToAccountDTO(account);

            return accountDTO;
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

    public DebitCardDTO createDebitCard(Account account) {
        DebitCard existingActiveCard = debitCardRepository.findByAccountAndIsActive(account, true);
        if (existingActiveCard != null) {
            existingActiveCard.setActive(false);
            debitCardRepository.save(existingActiveCard);
        }

        DebitCard newCard = new DebitCard();
        newCard.setCardNumber(generateDebitCardNumber());
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(false);
        newCard.setUuid(account.getCardUUID());
        newCard.setAccount(account);

        DebitCard savedCard = debitCardRepository.save(newCard);
        account.setCardNumber(newCard.getCardNumber()); // Update debitCardNumber in Account
        accountRepository.save(account);
        return mapToDebitCardDTO(savedCard);
    }

    public void deactivatePreviousCard(Account account) {
        DebitCard previousCard = debitCardRepository.findByAccountAndIsActive(account, true);
        if (previousCard != null) {
            previousCard.setActive(false);
            debitCardRepository.save(previousCard);
        }
    }

    public DebitCardDTO activateDebitCard(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuidAndIsActive(cardUUID, false);
        if(card == null && card.getAccount().getPin() != pin)
            return null;

        deactivatePreviousCard(card.getAccount());
        card.setActive(true);
        DebitCard savedCard = debitCardRepository.save(card);
        return mapToDebitCardDTO(savedCard);
    }

//    public void deactivateDebitCard(String iban, String cardNumber, boolean active){
//        try {
//            Account  account = accountRepository.findByIban(iban);
//            if(account == null)
//                throw new ServiceException("Invalid IBAN");
//
//            DebitCard debitCard = debitCardRepository.findByAccountAndIsActive(account, active);
//            if (debitCard == null && !debitCard.getCardNumber().equals(cardNumber))
//                throw new ServiceException("Invalid debit card details");
//            debitCard.setActive(active);
//            accountRepository.save(account);
//        }catch (Exception ex){
//            throw new ServiceException("Failed to deactivate debit card", ex);
//        }
//    }

    public void deactivateDebitCard(String iban, String cardNumber, boolean active) {
        try {
            Account account = accountRepository.findByIban(iban);
            if (account == null) {
                throw new ServiceException("Invalid IBAN");
            }

            DebitCard debitCard = debitCardRepository.findByAccountAndCardNumber(account, cardNumber);
            if (debitCard == null) {
                throw new ServiceException("Invalid debit card number");
            }

            debitCard.setActive(active);
            debitCardRepository.save(debitCard);
        } catch (Exception ex) {
            throw new ServiceException("Failed to deactivate debit card", ex);
        }
    }

    public boolean verifyCardCredentials(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuid(cardUUID);
        return card != null && card.getAccount().getPin().equals(pin);
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
        accountDTO.setDebitCardNumber(account.getCardNumber());

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

    protected String generateIban() {
        StringBuilder ibanBuilder = new StringBuilder(IBAN_PREFIX);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randomDigit = random.nextInt(10);
            ibanBuilder.append(randomDigit);
        }
        return ibanBuilder.toString();
    }

    public String generateCardUUID() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String generateDebitCardNumber() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        sb.append("4");
        for (int i = 0; i < 3; i++)
            sb.append(random.nextInt(10));

        for (int i = 0; i < 12; i++)
            sb.append(random.nextInt(10));

        return sb.toString();
    }

    private DebitCardDTO mapToDebitCardDTO(DebitCard card) {
        DebitCardDTO cardDTO = new DebitCardDTO();
        cardDTO.setCardNumber(card.getCardNumber());
        return cardDTO;
    }
}
