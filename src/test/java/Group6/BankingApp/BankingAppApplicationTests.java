package Group6.BankingApp;

import Group6.BankingApp.Controllers.AccountController;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
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

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		AccountDTO expectedAccount = new AccountDTO(newAccount, userService.getUserById(newAccount.getUserId()));
		Mockito.when(accountService.addAccount(newAccount)).thenReturn(expectedAccount);


		ResponseEntity<AccountDTO> response = accountController.createAccount(newAccount);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expectedAccount, response.getBody());
	}

	@Test
	public void testCreateAccount_InvalidRequest() {
		// Set up the mock behavior of the accountService to return null or an invalid response
		Mockito.when(accountService.addAccount(null)).thenReturn(null);

		ResponseEntity<AccountDTO> response = accountController.createAccount(null);

		// Assert that the response status code is BAD_REQUEST
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		// Assert that the response body is null or empty
		assertNull(response.getBody());
	}

	@Test
	public void testCreateAccount_Exception() {
		NewAccountDTO newAccountDTO = new NewAccountDTO();

		Mockito.when(accountService.addAccount(newAccountDTO)).thenThrow(new RuntimeException("Something went wrong"));

		ResponseEntity<AccountDTO> response = accountController.createAccount(newAccountDTO);

		// Assert that the response status code is INTERNAL_SERVER_ERROR
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
