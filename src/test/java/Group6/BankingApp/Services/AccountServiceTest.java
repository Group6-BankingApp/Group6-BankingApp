package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private DebitCardRepository debitCardRepository;

    @InjectMocks
    @Autowired
    private AccountService accountService;


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testAddAccount() {
//
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setIban("NL01INHO9501054837");
//
//        UserDTO2 userDTO2 = new UserDTO2();
//        userDTO2.setId(123L);
//
//        accountDTO.setUser(userDTO2);
//
//        Account result = accountService.addAccount(accountDTO);
//
//        ResponseEntity<Account> response = ResponseEntity.ok(result);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(result, response.getBody());
//    }

//    @Test
//    void testAddAccount() {
//
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setIban("NL01INHO9501054837");
//
//        UserDTO2 userDTO2 = new UserDTO2();
//        userDTO2.setId(123L);
//
//        accountDTO.setUser(userDTO2);
//
//        Account result = accountService.addAccount(accountDTO);
//
//        ResponseEntity<Account> response = ResponseEntity.ok(result);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(result, response.getBody());
//    }


//    @Test
//    void testUpdateAccountByIban() {
//
//        String iban = "NL01INHO9501054837";
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setAccountType("Savings");
//        accountDTO.setDailyLimit(1000.0);
//        accountDTO.setPin("1234");
//
//        Account accountToUpdate = new Account();
//        accountToUpdate.setIban(iban);
//
//        Mockito.when(accountRepository.findById(iban)).thenReturn(Optional.of(accountToUpdate));
//        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountToUpdate);
//
//        Account updatedAccount = accountService.updateAccountByIban(iban, accountDTO);
//
//        ResponseEntity<Account> response = ResponseEntity.ok(updatedAccount);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(updatedAccount, response.getBody());
//    }
    @Test
    void testMapToAccountDTO() {

        Account account = new Account();
        account.setIban("NL01INHO9501054837");

        User user = new User();
        user.setId(123L);

        account.setUser(user);

        AccountDTO accountDTO = accountService.mapToAccountDTO(account);


        assertEquals(account.getIban(), accountDTO.getIban());
        assertEquals(account.getAccountType(), accountDTO.getAccountType());
        assertEquals(account.getCardUUID(), accountDTO.getCardUUID());
        assertEquals(account.getPin(), accountDTO.getPin());
        assertEquals(account.getDailyLimit(), accountDTO.getDailyLimit());
    }


    @Test
    void testDeactivateDebitCard() {

        String iban = "NL01INHO9501054837";
        boolean active = true;
        DebitCardDTO debitCardDTO = new DebitCardDTO();
        debitCardDTO.setCardNumber("123456789");

        Account account = new Account();
        account.setIban(iban);
        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber(debitCardDTO.getCardNumber());
        debitCard.setActive(true);
        account.setDebitCard(debitCard);

        Mockito.when(accountRepository.findByIban(iban)).thenReturn(account);

        accountService.deactivateDebitCard(iban, debitCardDTO.getCardNumber() , active);

        assertEquals(false, debitCard.isActive());

        ResponseEntity<Void> response = ResponseEntity.ok().build();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testMapToDebitCardDTO() {

        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber("1234567890");

        DebitCardDTO debitCardDTO = accountService.mapToDebitCardDTO(debitCard);

        assertNotNull(debitCardDTO);

        assertEquals("1234567890", debitCardDTO.getCardNumber());
    }

    private static final String IBAN_PREFIX = "NL01INHO";

    @Test
    public void testGenerateIban() {

        String generatedIban = accountService.generateIban();

        // Check if the generated IBAN has the correct format
        assertTrue(generatedIban.startsWith(IBAN_PREFIX));
        assertEquals(18, generatedIban.length());
    }

    @Test
    void testGenerateCardUUID() {

        String cardUUID = accountService.generateCardUUID();

        assertNotNull(cardUUID);

        assertTrue(isValidUUID(cardUUID));
    }

    @Test
    public void testGenerateDebitCardNumber() {
        String debitCardNumber = accountService.generateDebitCardNumber();

        // Assert that the generated debit card number is valid
        assertEquals(16, debitCardNumber.length());
        assertEquals("4", debitCardNumber.substring(0, 1)); // Asserts the first character is "4"
        for (int i = 1; i < debitCardNumber.length(); i++) {
            int digit = Character.getNumericValue(debitCardNumber.charAt(i));
            assertEquals(true, digit >= 0 && digit <= 9); // Asserts that all other characters are digits (0-9)
        }
    }

    @Test
    public void testCreateDebitCard() {


        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        account.setCardUUID("618cfb19-02bc-4896-b510-6943a6996026");
        account.setPin("1234");
        account.setBalance(1000.0);


        DebitCard existingActiveCard = new DebitCard();
        existingActiveCard.setCardNumber("6638545966347381");
        existingActiveCard.setExpirationDate(LocalDate.now().plusYears(5));
        existingActiveCard.setActive(true);
        existingActiveCard.setUuid(account.getCardUUID());
        existingActiveCard.setAccount(account);


        DebitCard newCard = new DebitCard();
        newCard.setCardNumber("5538545966347381");
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(false);
        newCard.setUuid(account.getCardUUID());
        newCard.setAccount(account);


        DebitCard savedCard = new DebitCard();
        savedCard.setId(1L);
        savedCard.setCardNumber(newCard.getCardNumber());
        savedCard.setExpirationDate(newCard.getExpirationDate());
        savedCard.setActive(newCard.isActive());
        savedCard.setUuid(newCard.getUuid());
        savedCard.setAccount(newCard.getAccount());


        Mockito.when(debitCardRepository.findByAccountAndIsActive(account, true)).thenReturn(existingActiveCard);
        Mockito.when(debitCardRepository.save(existingActiveCard)).thenReturn(existingActiveCard);
        Mockito.when(debitCardRepository.save(newCard)).thenReturn(savedCard);


        Mockito.when(accountRepository.save(account)).thenReturn(account);


        DebitCardDTO result = accountService.createDebitCard(account);


        assertEquals(savedCard.getCardNumber(), result.getCardNumber());


        Mockito.verify(debitCardRepository, Mockito.times(1)).findByAccountAndIsActive(account, true);
        Mockito.verify(debitCardRepository, Mockito.times(1)).save(existingActiveCard);
        Mockito.verify(debitCardRepository, Mockito.times(1)).save(newCard);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);

    }

    @Test
    public void testUpdatePin() {
        // Create a sample AccountDTO with updated pin
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setPin("1122");

        // Create a sample Account with existing pin
        Account account = new Account();
        account.setPin("2468");

        // Mock the behavior of the accountRepository
        when(accountRepository.findById("NL01INHO9501054837")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        // Call the updatePin method
        NewAccountDTO updatedAccountDTO = accountService.updatePin("NL01INHO9501054837", accountDTO);

        // Assert that the pin has been updated
        assertEquals(accountDTO.getPin(), updatedAccountDTO.getPin());
    }

    private boolean isValidUUID(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Test
    public void testGetAccountsByCustomerId() {

        Long customerId = 1L;
        User user = new User();
        user.setId(customerId);

        Account account1 = new Account();
        account1.setUser(user);

        Account account2 = new Account();
        account2.setUser(user);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        when(accountRepository.findAllByUserId(customerId)).thenReturn(accounts);

        List<AccountDTO> accountDTOs = accountService.getAccountsByCustomerId(customerId);

        assertEquals(accounts.size(), accountDTOs.size());
    }

    @Test
    public void testDeactivatePreviousCard() {
        Account account = new Account();
        DebitCard previousCard = new DebitCard();
        previousCard.setActive(true);
        previousCard.setAccount(account);

        when(debitCardRepository.findByAccountAndIsActive(account, true)).thenReturn(previousCard);

        accountService.deactivatePreviousCard(account);

        assertFalse(previousCard.isActive());
        verify(debitCardRepository, times(1)).save(previousCard);
    }
}