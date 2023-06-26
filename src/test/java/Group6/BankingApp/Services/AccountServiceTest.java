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
import Group6.BankingApp.Models.dto.UserDTO2;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private DebitCardService debitCardService;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddAccount() {
        // Arrange
        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setUserId(1L);
        newAccountDTO.setAccountType("Current");
        newAccountDTO.setPin("1234");
        newAccountDTO.setDailyLimit(1000.0);
        newAccountDTO.setAbsoluteLimit(2000.0);
        newAccountDTO.setTransactionLimit(100.0);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        // Act
        AccountDTO accountDTO = accountService.addAccount(newAccountDTO);

        // Assert
        assertNotNull(accountDTO);

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testAddAccount_InvalidUser() {
        // Arrange
        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setUserId(1L);
        // Simulate user not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ServiceException.class, () -> {
            accountService.addAccount(newAccountDTO);
        });

        // Verify
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testUpdateAccountByIban() {
        // Arrange
        String iban = "NL01INH1234567890";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban("NL01INH1234567890");
        UserDTO2 userDTO = new UserDTO2();
        userDTO.setId(1L);
        accountDTO.setAccountType("Current");
        accountDTO.setPin("1234");
        accountDTO.setDailyLimit(1000.0);
        accountDTO.setBalance(500.0);
        accountDTO.setAbsoluteLimit(2000.0);
        accountDTO.setTransactionLimit(100.0);

        Account existingAccount = new Account();
        existingAccount.setIban("NL01INH1234567890");
        User existingUser = new User();
        existingUser.setId(1L);
        existingAccount.setUser(existingUser);

        when(accountRepository.findById(iban)).thenReturn(java.util.Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        // Act
        NewAccountDTO updatedAccountDTO = accountService.updateAccountByIban(iban, existingAccount);

        // Assert
        assertNotNull(updatedAccountDTO);
        assertEquals(existingAccount.getUser().getId(), updatedAccountDTO.getUserId());

        // Verify
        verify(accountRepository, times(1)).findById(iban);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testUpdateAccountByIban_AccountNotFound() {
        // Arrange
        String iban = "NL01INH1234567890";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIban("NL01INH1234567890");
        UserDTO2 userDTO = new UserDTO2();
        userDTO.setId(1L);
        // Simulate account not found
        when(accountRepository.findById(iban)).thenReturn(Optional.empty());

        // Act & Assert
//        assertThrows(ServiceException.class, () -> {
//            accountService.updateAccountByIban(iban, accountDTO);
//        });

        // Verify
        verify(accountRepository, times(1)).findById(iban);
        verify(accountRepository, never()).save(any(Account.class));
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
        assertEquals(account.getPin(), accountDTO.getPin());
        assertEquals(account.getDailyLimit(), accountDTO.getDailyLimit());
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

        String cardUUID = debitCardService.generateCardUUID();

        assertNotNull(cardUUID);

        assertTrue(isValidUUID(cardUUID));
    }

    @Test
    public void testGenerateDebitCardNumber() {
        String debitCardNumber = debitCardService.generateDebitCardNumber();

        // Assert that the generated debit card number is valid
        assertEquals(16, debitCardNumber.length());
        assertEquals("4", debitCardNumber.substring(0, 1)); // Asserts the first character is "4"
        for (int i = 1; i < debitCardNumber.length(); i++) {
            int digit = Character.getNumericValue(debitCardNumber.charAt(i));
            assertEquals(true, digit >= 0 && digit <= 9); // Asserts that all other characters are digits (0-9)
        }
    }

    @Test
    void testCreateDebitCard() {
        // Arrange
        Account account = new Account();
        account.setAccountType("Current");

        // Create a new debit card
        DebitCard newCard = new DebitCard();
        newCard.setAccount(account);

        // Mock the repository method calls
        when(debitCardRepository.save(any(DebitCard.class))).thenReturn(newCard);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // Act
        DebitCardDTO result = debitCardService.createDebitCard(account);

        // Assert
        assertNotNull(result);
        assertEquals(newCard.getId(), result.getCardNumber());
        assertEquals(newCard.getCardNumber(), result.getCardNumber());
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

        List<AccountDTO> accountDTOs = accountService.getCurrentAccountsByCustomerId(customerId);

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

    @Test
    void deactivateDebitCard_ValidIbanAndCardNumber() {
        // Arrange
        String iban = "NL01INH1234567890";
        String cardNumber = "41111111111111";
        boolean active = false;

        Account account = new Account();
        DebitCard debitCard = new DebitCard();
        debitCard.setActive(true);

        when(accountRepository.findByIban(iban)).thenReturn(account);
        when(debitCardRepository.findByAccountAndCardNumber(account, cardNumber)).thenReturn(debitCard);

        // Act
        accountService.deactivateDebitCard(iban, cardNumber, active);

        // Assert
        assertFalse(debitCard.isActive());
        verify(debitCardRepository, times(1)).save(debitCard);
    }

    @Test
    void deactivateDebitCard_InvalidIban() {
        // Arrange
        String iban = "NL01INH1234567890";
        String cardNumber = "1111111111111111";
        boolean active = false;

        when(accountRepository.findByIban(iban)).thenReturn(null);

        // Act and Assert
        assertThrows(ServiceException.class, () -> accountService.deactivateDebitCard(iban, cardNumber, active));
        verify(debitCardRepository, never()).findByAccountAndCardNumber(any(), any());
        verify(debitCardRepository, never()).save(any());
    }

    @Test
    void deactivateDebitCard_InvalidCardNumber() {
        // Arrange
        String iban = "NL01INH1234567890";
        String cardNumber = "1111111111111111";
        boolean active = false;

        Account account = new Account();

        when(accountRepository.findByIban(iban)).thenReturn(account);
        when(debitCardRepository.findByAccountAndCardNumber(account, cardNumber)).thenReturn(null);

        // Act and Assert
        assertThrows(ServiceException.class, () -> accountService.deactivateDebitCard(iban, cardNumber, active));
        verify(debitCardRepository, times(1)).findByAccountAndCardNumber(account, cardNumber);
        verify(debitCardRepository, never()).save(any());
    }

    @Test
    void testDeactivateDebitCard() {
        // Create a sample account and debit card
        Account account = new Account();
        account.setIban("NL01INH1234567890");

        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber("41111111111111");
        debitCard.setActive(true);

        // Mock the behavior of the repositories
        Mockito.when(accountRepository.findByIban(Mockito.anyString())).thenReturn(account);
        Mockito.when(debitCardRepository.findByAccountAndCardNumber(Mockito.any(), Mockito.anyString())).thenReturn(debitCard);
        Mockito.when(debitCardRepository.save(Mockito.any())).thenReturn(debitCard);

        // Deactivate the debit card
        accountService.deactivateDebitCard("NL01INH1234567890", "41111111111111", false);

        // Verify
        Mockito.verify(debitCardRepository).save(debitCard);
        // Assert
        Assertions.assertFalse(debitCard.isActive());
    }

    private boolean isValidUUID(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}