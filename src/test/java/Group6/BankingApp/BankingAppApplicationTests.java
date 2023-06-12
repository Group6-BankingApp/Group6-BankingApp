package Group6.BankingApp;

import Group6.BankingApp.Controllers.AccountController;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankingAppApplicationTests {

	@Test
	void contextLoads() {
	}

	//Accounts
	@Mock
	private AccountService accountService;

	@Mock
	private UserService userService;

	@InjectMocks
	private AccountController accountController;

	@Test
	public void testGetAccounts_Success() {
		int skip = 0;
		int limit = 10;
		List<AccountDTO> expectedAccounts = Collections.singletonList(new AccountDTO(/* account data */));
		Mockito.when(accountService.findAllAccounts(skip, limit)).thenReturn(expectedAccounts);

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAllAccounts(skip, limit);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedAccounts, response.getBody());
	}

	@Test
	public void testGetAccounts_InvalidSkip() {
		int skip = -1;
		int limit = 10;

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAllAccounts(skip, limit);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetAccounts_InvalidLimit() {
		int skip = 0;
		int limit = 51;

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAllAccounts(skip, limit);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetAccounts_Exception() {
		int skip = 0;
		int limit = 10;
		Mockito.when(accountService.findAllAccounts(skip, limit)).thenThrow(new RuntimeException("Some error message"));

		// test
		ResponseStatusException exception =
				org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
					accountController.getAllAccounts(skip, limit);
				});
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
		assertEquals("Failed to retrieve accounts", exception.getReason());
	}

	@Test
	public void testCreateAccount_Success() {
		NewAccountDTO newAccount = new NewAccountDTO();
		AccountDTO expectedAccount = new AccountDTO();
		Mockito.when(accountService.addAccount(newAccount)).thenReturn(expectedAccount);


		ResponseEntity<AccountDTO> response = accountController.createAccount(newAccount);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expectedAccount, response.getBody());
	}

	@Test
	public void testCreateAccount_InvalidRequest() {
		Mockito.when(accountService.addAccount(null)).thenReturn(null);

		ResponseEntity<AccountDTO> response = accountController.createAccount(null);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testCreateAccount_Exception() {
		NewAccountDTO newAccountDTO = new NewAccountDTO();

		Mockito.when(accountService.addAccount(newAccountDTO)).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<AccountDTO> response = accountController.createAccount(newAccountDTO);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testGetAccountByIban_AccountFound() {
		String iban = "123456789";
		AccountDTO expectedAccount = new AccountDTO();

		Mockito.when(accountService.getAccountByIban(iban)).thenReturn(expectedAccount);

		ResponseEntity<AccountDTO> response = accountController.getAccountByIban(iban);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedAccount, response.getBody());
	}

	@Test
	public void testGetAccountByIban_Exception() {
		String iban = "123456789";

		Mockito.when(accountService.getAccountByIban(iban)).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<AccountDTO> response = accountController.getAccountByIban(iban);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testUpdateAccountByIban_Success() {
		String iban = "123456789";
		AccountDTO accountDTO = new AccountDTO();
		NewAccountDTO updatedAccountDTO = new NewAccountDTO();

		Mockito.when(accountService.updateAccountByIban(iban, accountDTO)).thenReturn(updatedAccountDTO);

		ResponseEntity<NewAccountDTO> response = accountController.updateAccountByIban(iban, accountDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedAccountDTO, response.getBody());
	}

	@Test
	public void testDeleteAccountByIban_Success() {
		String iban = "123456789";

		Mockito.doNothing().when(accountService).deleteAccount(iban);

		ResponseEntity<Void> response = accountController.deleteAccountByIban(iban);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetAccountBalance_Success() {
		String iban = "123456789";
		String pin = "1234";
		double expectedBalance = 1000.0;

		Mockito.when(accountService.getAccountBalance(iban, pin)).thenReturn(expectedBalance);

		double result = accountController.getAccountBalance(iban, pin);

		assertEquals(expectedBalance, result);
	}

	@Test
	public void testDeactivateDebitCard_Success() {
		String iban = "123456789"; // Sample IBAN
		DebitCardDTO debitCardDTO = new DebitCardDTO();

		Mockito.doNothing().when(accountService).deactivateDebitCard(iban, debitCardDTO, true);

		ResponseEntity<Void> response = accountController.deactivateDebitCard(iban, debitCardDTO, true);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeactivateDebitCard_Failure() {
		String iban = "123456789";
		DebitCardDTO debitCardDTO = null;

		ResponseEntity<Void> response = accountController.deactivateDebitCard(iban, debitCardDTO, true);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testUpdatePin_Success() {
		String iban = "123456789";
		AccountDTO accountDTO = new AccountDTO();

		NewAccountDTO updatedAccountDTO = new NewAccountDTO();
		Mockito.when(accountService.updatePin(iban, accountDTO)).thenReturn(updatedAccountDTO);

		ResponseEntity<NewAccountDTO> response = accountController.updatePin(iban, accountDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedAccountDTO, response.getBody());
	}
}
