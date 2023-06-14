package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import org.hibernate.service.spi.ServiceException;
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
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

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

    //WORKS
    @Test
    void testMapToAccountDTO() {

        Account account = new Account();
        account.setIban("NL01INHO9501054837");

        User user = new User();
        user.setId(123L);

        account.setUser(user);

        AccountDTO accountDTO = accountService.mapToAccountDTO(account);


        assertEquals(account.getIban(), accountDTO.getIban());
        assertEquals(user.getId(), accountDTO.getUser().getId());
        assertEquals(account.getAccountType(), accountDTO.getAccountType());
        assertEquals(account.getCardUUID(), accountDTO.getCardUUID());
        assertEquals(account.getPin(), accountDTO.getPin());
        assertEquals(account.getDailyLimit(), accountDTO.getDailyLimit());
    }
    // wWORKS - negative condition
    @Test
    void testMapToAccountDTO_NullAccount() {

        AccountDTO accountDTO = accountService.mapToAccountDTO(null);

        assertNull(accountDTO);
    }

    //WORKS
    @Test
    void testGetAccountByIban_ValidIban() {
        // Create a sample Account object with test data
        Account account = new Account();
        account.setIban("NL01INHO9501054837");
        account.setAccountType("Savings");
        // ... Set other properties as needed

        // Mock the accountRepository to return the sample Account when findById is called
        when(accountRepository.findById("NL01INHO9501054837")).thenReturn(Optional.of(account));

        // Call the getAccountByIban method with a valid iban
        AccountDTO accountDTO = accountService.getAccountByIban("NL01INHO9501054837");

        // Assert that the AccountDTO is not null
        assertNotNull(accountDTO);

        // Assert that the account properties are mapped correctly
        assertEquals("NL01INHO9501054837", accountDTO.getIban());
        assertEquals("Savings", accountDTO.getAccountType());
        // ... Assert other properties as needed
    }

  //works -negative condition
    @Test
    void testGetAccountByIban_NonExistentIban() {
        String nonExistentIban = "NL01INHO9501054837";

        // Create a mock account that represents a non-existent account
        Account nonExistentAccount = null;

        // Mock the accountRepository to return the mock non-existent account when findById is called with the non-existent IBAN
        when(accountRepository.findById(nonExistentIban)).thenReturn(Optional.ofNullable(nonExistentAccount));

        ServiceException exception = assertThrows(ServiceException.class, () -> accountService.getAccountByIban(nonExistentIban));

        assertTrue(exception.getMessage().contains("Account not found"));
        System.out.println(exception.getMessage());
    }

    //WORKS
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

        accountService.deactivateDebitCard(iban, debitCardDTO, active);

        assertEquals(active, debitCard.isActive());

        ResponseEntity<Void> response = ResponseEntity.ok().build();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    // WORKS
    @Test
    public void testGetAllAccounts() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        user1.setPassword("123456");
        user1.setPhoneNumber("0612345678");
        user1.setHasAccount(true);
        user1.setRoles(List.of(Role.ROLE_ADMIN));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@gmail.com");
        user2.setPassword("123456");
        user2.setPhoneNumber("0612345678");
        user2.setHasAccount(true);
        user2.setRoles(List.of(Role.ROLE_USER));

        List<User> users = Arrays.asList(user1, user2);

        DebitCard debitCard1 = new DebitCard("1111222233334444", LocalDate.now().plusYears(3), "123", "John Doe", true, "UUID1");
        DebitCard debitCard2 = new DebitCard("5555666677778888", LocalDate.now().plusYears(2), "456", "Jane Smith", true, "UUID2");



        List<Account> accounts = Arrays.asList(
                new Account("NL01INHO9501054837",user1,"Savings", accountService.generateCardUUID(), "1234", 1000.0, 600.0, 0, true, debitCard1),
                new Account("NL01INHO9501054837",user2,"Savings", accountService.generateCardUUID(), "1234", 1000.0, 600.0, 0, true, debitCard1)
        );

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        verify(accountRepository, times(1)).findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(accounts.get(0), result.get(0));
        assertEquals(accounts.get(1), result.get(1));
    }
    //WORKS
    @Test
    public void testFindAccountByIban() {
        // Set up test data
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        user1.setPassword("123456");
        user1.setPhoneNumber("0612345678");
        user1.setHasAccount(true);
        user1.setRoles(List.of(Role.ROLE_ADMIN));

        DebitCard debitCard1 = new DebitCard("1111222233334444", LocalDate.now().plusYears(3), "123", "John Doe", true, "UUID1");

        String testIban = "NL01INHO9501054837";
        Account testAccount = new Account("NL01INHO9501054837", user1, "Savings", accountService.generateCardUUID(), "1234", 1000.0, 600.0, 0, true, debitCard1);

        // Mock the behavior of the account repository
        when(accountRepository.findByIban(testIban)).thenReturn(testAccount);

        // Call the method being tested
        Account result = accountService.findAccountByIban(testIban);

        // Verify the expected behavior
        assertNotNull(result);
        assertEquals(testIban, result.getIban());

        // Verify that the account repository method was called
        verify(accountRepository, times(1)).findByIban(testIban);
    }

    //works
    @Test
    void testMapToDebitCardDTO() {

        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber("1234567890");

        DebitCardDTO debitCardDTO = accountService.mapToDebitCardDTO(debitCard);

        assertNotNull(debitCardDTO);

        assertEquals("1234567890", debitCardDTO.getCardNumber());
    }

    //works
    @Test
    void testDeleteAccount() {

        String iban = "NL01INHO9501054837";
        when(accountRepository.findById(iban)).thenReturn(Optional.of(new Account()));
        accountService.deleteAccount(iban);
        verify(accountRepository, times(1)).deleteById(iban);
    }

    //works - negative condition
    @Test
    void testDeleteAccount_NonExistentAccount() {
        String nonExistentIban = "NL99NONEXISTENT";

        // Mock the accountRepository to return an empty Optional when findById is called with the non-existent IBAN
        when(accountRepository.findById(nonExistentIban)).thenReturn(Optional.empty());

        // Call the deleteAccount method with the non-existent IBAN and expect a ServiceException
        ServiceException exception = assertThrows(ServiceException.class, () -> accountService.deleteAccount(nonExistentIban));

        // Verify that the deleteById method of accountRepository was not called
        verify(accountRepository, times(0)).deleteById(nonExistentIban);

        assertEquals("Account not found", exception.getMessage());
        System.out.println(exception.getMessage());
    }

}