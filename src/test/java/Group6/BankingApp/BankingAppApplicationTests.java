package Group6.BankingApp;

import Group6.BankingApp.Controllers.AccountController;
import Group6.BankingApp.Controllers.UserController;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.*;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BankingAppApplicationTests {

	@Test
	void contextLoads() {
	}

	//Users

	private MockMvc mockMvc;


	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	private UserDTO2 userDTO2;

	@BeforeEach
	void setUp() {
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("Doe");
		user1.setEmail("john.doe@gmail.com");
		user1.setPassword("123456");
		user1.setPhoneNumber("0612345678");
		user1.setHasCurrentAccount(true);
		user1.setHasSavingsAccount(true);
		user1.setRoles(List.of(Role.ROLE_USER));
		userDTO2 = new UserDTO2(user1);

		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	void getAllUsersTest() throws Exception {
		when(userService.getAllUsers()).thenReturn(List.of(userDTO2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].firstName").value("John"))
				.andExpect(jsonPath("$[0].lastName").value("Doe"));
	}

	@Test
	void getAllUsersWithAccountTest() throws Exception{
		when(userService.getAllUsersWithAccount()).thenReturn(List.of(userDTO2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/withAccount")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].firstName").value("John"))
				.andExpect(jsonPath("$[0].lastName").value("Doe"));
	}

	@Test
	void getUserByIdTest() throws Exception{
		when(userService.getUserById(any(Long.class))).thenReturn(userDTO2);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	void addUserTest() throws Exception{
		when(userService.addUser(any(UserDTO.class))).thenReturn(userDTO2);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	void updateUserTest() throws Exception{
		when(userService.updateUser(any(Long.class),any(UserDTO.class))).thenReturn(userDTO2);
		this.mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("John"))
				.andExpect(jsonPath("$.lastName").value("Doe"));
	}

	@Test
	void deleteUserTest() throws Exception{
		doNothing().when(userService).deleteUser(any(Long.class));
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
				.andExpect(status().isOk());
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
		Account account = new Account();
		NewAccountDTO updatedAccountDTO = new NewAccountDTO();

		Mockito.when(accountService.updateAccountByIban(iban, account)).thenReturn(updatedAccountDTO);

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
		String debitCardNum = "123456789";

		Mockito.doNothing().when(accountService).deactivateDebitCard(iban, debitCardNum, true);

		ResponseEntity<String> response = accountController.deactivateDebitCard(iban, debitCardNum);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), "Debit card deactivated successfully.");
	}

	@Test
	public void testDeactivateDebitCard_Failure() {
		String iban = "123456789";
		String debitCardNum = null;

		ResponseEntity<String> response = accountController.deactivateDebitCard(iban, debitCardNum);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

//	@Test
//	public void testUpdatePin_Success() {
//		String iban = "123456789";
//		AccountDTO accountDTO = new AccountDTO();
//
//		NewAccountDTO updatedAccountDTO = new NewAccountDTO();
//		Mockito.when(accountService.updatePin(iban, accountDTO)).thenReturn(updatedAccountDTO);
//
//		ResponseEntity<NewAccountDTO> response = accountController.updatePin(iban, accountDTO);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(updatedAccountDTO, response.getBody());
//	}
}
