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
import jakarta.transaction.Transactional;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    public CreatedAccountsDTO addAccount(NewAccountDTO newAccountDTO) {

        try {
            String iban = generateIban();
            String savingsIban = generateIban();
            Long userId = newAccountDTO.getUserId();
            User user = userService.getFullUserById(userId);
            if (user == null)
                throw new ServiceException("User with ID " + userId + " does not exist.");


            String cardUUID = generateCardUUID();
            String pin = newAccountDTO.getPin();
            double dailyLimit = newAccountDTO.getDailyLimit();

            Account account = new Account(iban, user, "Current", cardUUID, pin, dailyLimit, newAccountDTO.getBalance(), newAccountDTO.getAbsoluteLimit(), newAccountDTO.getTransactionLimit(), true, null);
            user.setHasCurrentAccount(true);
            Account savingsAccount = new Account(savingsIban, user, "Savings", cardUUID, pin, 200, 0, 0, 100, false, null);
            user.setHasSavingsAccount(true);
            userRepository.save(user);
            accountRepository.save(account);
            accountRepository.save(savingsAccount);



            AccountDTO accountDTO = mapToAccountDTO(account);
            AccountDTO savingsAccountDTO = mapToAccountDTO(savingsAccount);
            CreatedAccountsDTO createdAccountsDTO = new CreatedAccountsDTO(accountDTO, savingsAccountDTO);
            return createdAccountsDTO;
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
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

    public DebitCardDTO createDebitCard(Account account) {
        if(!"Current".equals(account.getAccountType())){
            throw new ServiceException("Debit card can only be created for 'Current' accounts!");
        }
        deactivatePreviousCard(account);

        DebitCard newCard = createNewDebitCard(account);
        DebitCard savedCard = debitCardRepository.save(newCard);
        account.setDebitCard(savedCard);
        Account savedAccount = accountRepository.save(account);

        return mapToDebitCardDTO(savedCard);
    }

    private DebitCard createNewDebitCard(Account account) {
        DebitCard newCard = new DebitCard();
        newCard.setCardNumber(generateDebitCardNumber());
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(false);
        newCard.setUuid(account.getCardUUID());
        newCard.setAccount(account);
        return newCard;
    }

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

    public void deactivateDebitCard(String iban, String cardNumber, boolean active){
        try {
            Account account = findAccountByIban(iban);
            DebitCard debitCard = getDebitCardByAccountAndCardNumber(account, cardNumber);
            updateDebitCardStatus(debitCard, active);
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

    private void updateDebitCardStatus(DebitCard debitCard, boolean active){
        debitCard.setActive(active);
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
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban(account.getIban());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setCardUUID(account.getCardUUID());
        accountDTO.setPin(account.getPin());
        accountDTO.setDailyLimit(account.getDailyLimit());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setAbsoluteLimit(account.getAbsoluteLimit());
        accountDTO.setTransactionLimit(account.getTransactionLimit());
        accountDTO.setDebitCardNumber(account.getCardNumber());

        DebitCard debitCard = account.getDebitCard();
        if (debitCard != null) {
            accountDTO.setDebitCardNumber(debitCard.getCardNumber());
        }

        return accountDTO;
    }


    private UserDTO2 mapToUserDTO2(User user) {
        if (user == null) {
            return null;
        }
        UserDTO2 userDTO = new UserDTO2();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setHasAccount(user.isHasAccount() ? "Yes" : "No");
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

        for (int i = 0; i < 15; i++)
            sb.append(random.nextInt(10));

        return sb.toString();
    }

    protected DebitCardDTO mapToDebitCardDTO(DebitCard card) {
        DebitCardDTO cardDTO = new DebitCardDTO();
        cardDTO.setCardNumber(card.getCardNumber());
        return cardDTO;
    }

    public List<AccountDTO> getAccountsByCustomerId(Long id) {
        List<Account> accounts = accountRepository.findAllByUserId(id);
        List<AccountDTO> accountDTOS = new ArrayList<>();
        for (Account account : accounts) {
            accountDTOS.add(mapToAccountDTO(account));
        }
        return accountDTOS;
    }
}
