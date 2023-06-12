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
    //DOESNT WORK
    @Test
    void testAddAccount() {
        // Mock user service
        User user = new User();
        when(userService.getFullUserById(any(Long.class))).thenReturn(user);

        // Mock account repository
        Account savedAccount = new Account();
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Create input DTO
        NewAccountDTO newAccountDTO = new NewAccountDTO();
        newAccountDTO.setUserId(123L);
        newAccountDTO.setAccountType("Savings");
        newAccountDTO.setPin("1234");
        newAccountDTO.setDailyLimit(1000.0);

        // Call the addAccount method
        AccountDTO accountDTO = accountService.addAccount(newAccountDTO);

        // Assert the result
        assertNotNull(accountDTO);

        // Assert the returned ResponseEntity
        ResponseEntity<AccountDTO> response = ResponseEntity.ok(accountDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDTO, response.getBody());
    }

    //DOESNT WORK
    @Test
    void testUpdateAccountByIban() {
        String iban = "NL01INHO9501054837";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountType("Savings");
        accountDTO.setDailyLimit(1000.0);
        accountDTO.setPin("1234");

        Account accountToUpdate = new Account();
        accountToUpdate.setIban(iban);

        UserDTO2 userDTO2 = new UserDTO2();
       // userDTO2.setId(100L);

        accountDTO.setUser(userDTO2);

        Mockito.when(accountRepository.findById(iban)).thenReturn(Optional.of(accountToUpdate));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountToUpdate);

        NewAccountDTO updatedAccount = accountService.updateAccountByIban(iban, accountDTO);

        ResponseEntity<NewAccountDTO> response = ResponseEntity.ok(updatedAccount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccount, response.getBody());
    }







}