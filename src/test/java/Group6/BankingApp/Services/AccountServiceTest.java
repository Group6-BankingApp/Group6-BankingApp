package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    @Autowired
    private AccountService accountService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


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


    @Test
    void testUpdateAccountByIban() {

        String iban = "NL01INHO9501054837";
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountType("Savings");
        accountDTO.setDailyLimit(1000.0);
        accountDTO.setPin("1234");

        Account accountToUpdate = new Account();
        accountToUpdate.setIban(iban);

        Mockito.when(accountRepository.findById(iban)).thenReturn(Optional.of(accountToUpdate));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(accountToUpdate);

        NewAccountDTO updatedAccount = accountService.updateAccountByIban(iban, accountDTO);

        ResponseEntity<NewAccountDTO> response = ResponseEntity.ok(updatedAccount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccount, response.getBody());
    }

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


}