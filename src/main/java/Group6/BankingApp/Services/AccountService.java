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

import java.util.*;

@Service
public class AccountService {


    private final AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository, DebitCardRepository debitCardRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
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
            throw new ServiceException("Invalid IBAN!");
        return account;
    }


    public AccountDTO addAccount(NewAccountDTO newAccountDTO) {
        try {
            Long userId = newAccountDTO.getUserId();
            User user = validateUserExistence(userId);

            Account account = generateCurrentAccount(newAccountDTO, user);
            updateHasAccountProperty(user, account.getAccountType());
            accountRepository.save(account);

            AccountDTO accountDTO = mapToAccountDTO(account);

            return accountDTO;
        } catch (EntityNotFoundException ex) {
            throw new ServiceException("User not found", ex);
        }  catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }

    public SavingsAccountDTO addSavingsAccount(Long id) {
        try {
            Long userId = id;
            User user = validateUserExistence(userId);

            Account account = generateSavingsAccount(user);
            updateHasAccountProperty(user, account.getAccountType());
            accountRepository.save(account);

            SavingsAccountDTO accountDTO = mapToSavingsAccountDTO(account);

            return accountDTO;
        } catch (EntityNotFoundException ex) {
            throw new ServiceException("User not found", ex);
        }  catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }

    private Account generateSavingsAccount( User user) {
        String iban = generateIban();
        return new Account(iban, user, "Savings", "", 0, 0, 0, 0);
    }


    private Account generateCurrentAccount(NewAccountDTO newAccountDTO, User user) {
        String iban = generateIban();
        String pin = newAccountDTO.getPin();
        double dailyLimit = newAccountDTO.getDailyLimit();

        return new Account(iban, user, "Current", pin, dailyLimit, 0, newAccountDTO.getAbsoluteLimit(), newAccountDTO.getTransactionLimit());
    }


    private void updateHasAccountProperty(User user,String accountType) {
        if(accountType.equals("Current"))
            user.setHasCurrentAccount(true);
        else if(accountType.equals("Savings"))
            user.setHasSavingsAccount(true);
        else
            throw new ServiceException("Invalid account type!");
        userRepository.save(user);
    }

    private User validateUserExistence(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ServiceException("User with ID " + userId + " does not exist.");
        }
        return user;
    }

    public NewAccountDTO updateAccountByIban(String iban, Account account) {

        if(account == null)
            throw new ServiceException("This account does not exist!");

        User user = account.getUser();
        if(user == null)
            throw new ServiceException("User does not exist.");

        Long userId = account.getUser().getId();
        if(userId == null)
            throw new ServiceException("User ID: " + userId + " does not exist.");

        if(!userId.equals(user.getId()))
            throw new ServiceException("The user ID in the accountDTO does not match the user ID in the account");

        // Update the account from accountDTO


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
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        User user = account.getUser();
        if (user == null) {
            throw new IllegalArgumentException("User not found in the account");
        }

        // Update the account's pin
        account.setPin(accountDTO.getPin());

        Account updatedAccount = accountRepository.save(account);
        return new NewAccountDTO(updatedAccount);
    }

    public void deleteAccount(String iban) {
        Optional<Account> accountOptional = accountRepository.findById(iban);
        if (accountOptional.isPresent()) {
            accountRepository.deleteById(iban);
        } else {
            throw new ServiceException("Account not found");
        }
    }

//    public DebitCardDTO createDebitCard(Account account) {
//        if(!"Current".equals(account.getAccountType())){
//            throw new ServiceException("Debit card can only be created for 'Current' accounts!");
//        }
//        deactivatePreviousCard(account);
//
//        DebitCard newCard = createNewDebitCard(account);
//        DebitCard savedCard = debitCardRepository.save(newCard);
//        account.setHasCard(true);
//        Account savedAccount = accountRepository.save(account);
//
//        return mapToDebitCardDTO(savedCard);
//    }
//
//    private DebitCard createNewDebitCard(Account account) {
//        DebitCard newCard = new DebitCard();
//        newCard.setCardNumber(generateDebitCardNumber());
//        newCard.setExpirationDate(LocalDate.now().plusYears(5));
//        newCard.setActive(false);
//        newCard.setUuid(account.getCardUUID());
//        newCard.setAccount(account);
//        return newCard;
//    }

    public void deactivatePreviousCard(Account account) {
        DebitCard previousCard = debitCardRepository.findByAccountAndIsActive(account, true);
        if (previousCard != null) {
            previousCard.setActive(false);
            debitCardRepository.save(previousCard);
        }
    }

    public DebitCardDTO activateDebitCard(String cardUUID, String pin) {
        DebitCard card = getInactiveDebitCardByUUIDAndPin(cardUUID, pin);
        if (card == null) {
            return null;
        }

        deactivatePreviousCard(card.getAccount());
        card.setActive(true);
        DebitCard savedCard = debitCardRepository.save(card);
        return mapToDebitCardDTO(savedCard);
    }

    private DebitCard getInactiveDebitCardByUUIDAndPin(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuidAndIsActive(cardUUID, false);
        if (card != null && card.getAccount().getPin().equals(pin)) {
            return card;
        }
        return null;
    }

    public void deactivateDebitCard(String iban, String cardNumber, boolean cardStatus){
        try {
            Account account = findAccountByIban(iban);
            DebitCard debitCard = getDebitCardByAccountAndCardNumber(account, cardNumber);
            updateDebitCardStatus(debitCard, cardStatus);
            account.setHasCard(cardStatus);
            account.setCardNumber(null);
            accountRepository.save(account);
        }catch (Exception ex){
            throw new ServiceException("Failed to deactivate debit card", ex);
        }
    }

    private DebitCard getDebitCardByAccountAndCardNumber(Account account, String cardNumber){
        DebitCard debitCard = debitCardRepository.findByAccountAndCardNumber(account, cardNumber);
        if (debitCard == null)
            throw new ServiceException("Invalid debit card number!");
        return debitCard;
    }

    private void updateDebitCardStatus(DebitCard debitCard, boolean cardStatus){
        debitCard.setActive(cardStatus);
        debitCardRepository.save(debitCard);
    }

    public boolean verifyCardCredentials(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuid(cardUUID);
        return card != null && card.getAccount().getPin().equals(pin);
    }

    protected AccountDTO mapToAccountDTO(Account account) {
        if (account == null) {
            return null;
        }
        AccountDTO accountDTO = new AccountDTO(account);
        return accountDTO;
    }


    private UserDTO2 mapToUserDTO2(User user) {
        if (user == null) {
            return null;
        }
        UserDTO2 userDTO = new UserDTO2(user);
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

    protected DebitCardDTO mapToDebitCardDTO(DebitCard card) {
        if(card == null)
            throw new ServiceException("card does not exist.");

        String date = card.getExpirationDate().toString();
        DebitCardDTO cardDTO = new DebitCardDTO(card.getCardNumber(),card.getUuid(),date,card.isActive());
        return cardDTO;
    }

    public List<AccountDTO> getCurrentAccountsByCustomerId(Long id) {
        List<Account> accounts = accountRepository.findAllByUserId(id);
        List<AccountDTO> accountDTOS = new ArrayList<>();
        for (Account account : accounts) {
            if(account.getAccountType().equals("Current")){
                accountDTOS.add(mapToAccountDTO(account));
            }
        }
        return accountDTOS;
    }

    public List<SavingsAccountDTO> getSavingsAccountsByCustomerId(Long id) {
        List<Account> accounts = accountRepository.findAllByUserId(id);
        List<SavingsAccountDTO> savingsAccountDTOS = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getAccountType().equals("Savings")) {
                savingsAccountDTOS.add(mapToSavingsAccountDTO(account));
            }
        }
        return savingsAccountDTOS;
    }

    private SavingsAccountDTO mapToSavingsAccountDTO(Account account) {
        if (account == null) {
            return null;
        }
        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO(account);
        return savingsAccountDTO;
    }

    public List<AccountDTO> getAllAccountsByCustomerId(Long id) {
        try {
            List<Account> accounts = accountRepository.findAllByUserId(id);
            List<AccountDTO> accountDTOS = new ArrayList<>();
            for (Account account : accounts) {
                accountDTOS.add(mapToAccountDTO(account));
            }
            return accountDTOS;
        }catch (Exception ex){
            throw new ServiceException("Failed to get all accounts", ex);
        }
    }
}
