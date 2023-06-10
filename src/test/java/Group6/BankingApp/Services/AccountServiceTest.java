package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

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

        assertEquals(false, debitCard.isActive());

        ResponseEntity<Void> response = ResponseEntity.ok().build();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }


}