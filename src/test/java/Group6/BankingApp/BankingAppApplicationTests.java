package Group6.BankingApp;

import Group6.BankingApp.Controllers.AccountController;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Services.AccountService;
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

@SpringBootTest
class BankingAppApplicationTests {

	@Test
	void contextLoads() {
	}

	//Accounts
	@Mock
	private AccountService accountService;

	@InjectMocks
	private AccountController accountController;

	@Test
	public void testGetAccounts_Success() {
		int skip = 0;
		int limit = 10;
		List<AccountDTO> expectedAccounts = Collections.singletonList(new AccountDTO(/* account data */));
		Mockito.when(accountService.getAccountsWithSkipAndLimit(skip, limit)).thenReturn(expectedAccounts);

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAccounts(skip, limit);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedAccounts, response.getBody());
	}

	@Test
	public void testGetAccounts_InvalidSkip() {
		int skip = -1;
		int limit = 10;

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAccounts(skip, limit);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetAccounts_InvalidLimit() {
		int skip = 0;
		int limit = 51;

		// test
		ResponseEntity<List<AccountDTO>> response = accountController.getAccounts(skip, limit);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetAccounts_Exception() {
		int skip = 0;
		int limit = 10;
		Mockito.when(accountService.getAccountsWithSkipAndLimit(skip, limit)).thenThrow(new RuntimeException("Some error message"));

		// test
		ResponseStatusException exception =
				org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
					accountController.getAccounts(skip, limit);
				});
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
		assertEquals("Failed to retrieve accounts", exception.getReason());
	}

	@Test
	public void testCreateAccount_Success() {
		AccountDTO newAccount = new AccountDTO(/* account data */);
		String generatedIban = "generated-iban";
		Account createdAccount = new Account(/* created account data */);
		Mockito.when(accountService.addAccount(newAccount)).thenReturn(createdAccount);

		// test
		ResponseEntity<Account> response = accountController.createAccount(newAccount);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(createdAccount, response.getBody());
	}

	@Test
	public void testCreateAccount_InvalidRequest() {
		AccountDTO newAccount = null;

		// test
		ResponseEntity<Account> response = accountController.createAccount(newAccount);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testCreateAccount_Exception() {
		AccountDTO newAccount = new AccountDTO(/* account data */);
		Mockito.when(accountService.addAccount(newAccount)).thenThrow(new RuntimeException("Some error message"));

		// test
		ResponseEntity<Account> response = accountController.createAccount(newAccount);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
