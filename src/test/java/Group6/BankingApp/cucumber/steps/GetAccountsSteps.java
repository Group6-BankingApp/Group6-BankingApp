package Group6.BankingApp.cucumber.steps;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Models.dto.TokenDTO;
import Group6.BankingApp.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsSteps{
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<List<AccountDTO>> response;
    private ResponseEntity<AccountDTO> responseSingleAccountDTO;

    private final String baseURL = "http://localhost:8080";

    UserRepository userRepository;
    AccountRepository accountRepository;
    DebitCardRepository debitCardRepository;

    TokenDTO _tokenDTO;

    GetAccountsSteps(UserRepository userRepository, AccountRepository accountRepository, DebitCardRepository debitCardRepository){
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
    }

    @Given("I am logged in as a user")
    public void givenIAmLoggedInAsAUser() {
        String username = "john.doe@gmail.com";
        String password = "123456";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        RestTemplate restTemplate = new RestTemplate();

        // Set the login endpoint URL
        String loginUrl = "http://localhost:8080/users/login";

        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the JSON payload for login
        String jsonPayload = "{\"username\": \"john.doe@gmail.com\", \"password\": \"123456\"}";

        // Create the HTTP entity with request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        // Send the POST request to login
        ResponseEntity<TokenDTO> response = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        // Check the response status and perform further actions if needed
        if (response.getStatusCode().is2xxSuccessful()) {
            TokenDTO tokenDTO = response.getBody();

            _tokenDTO = tokenDTO;
            // Successfully logged in
            // Handle the tokenDTO or perform other actions
        }

    }

    @Given("there are accounts in the system")
    public void givenThereAreAccountsInTheSystem() {
        /*User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        String password = bCryptPasswordEncoder.encode("123456");
        user1.setPassword(password);
        user1.setPhoneNumber("0612345678");
        user1.setHasCurrentAccount(false);
        user1.setHasSavingsAccount(false);
        user1.setRoles(List.of(Role.ROLE_ADMIN));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@gmail.com");
        String password2 = bCryptPasswordEncoder.encode("123456");
        user2.setPassword(password2);
        user2.setPhoneNumber("0612345678");
        user2.setHasCurrentAccount(false);
        user2.setHasSavingsAccount(false);
        user2.setRoles(List.of(Role.ROLE_ADMIN));

        List<User> users = Arrays.asList(user1, user2);
        userRepository.saveAll(users);

        // Create and save Accounts
        Account account1 = new Account("NL01INHO9501054837",user1,"Current", "1234", 200.0, 1000.0, 0,100);
        Account account = new Account("NL01INHO9501054804",user1,"Savings", "1234", 1000.0, 1000.0, 0, 100);
        user1.setHasCurrentAccount(true);
        user1.setHasSavingsAccount(true);
        userRepository.save(user1);
        accountRepository.save(account);
        Account account2 = new Account("NL01INHO2371458805",user2, "Current", "5678", 2000.0, 950.0, -20,100);
        //account2.setDebitCard(debitCard2);

        Account account3 = new Account("NL01INH1234567890",user2, "Savings", "5678", 2000.0, 950.0, -100,100);
        //account2.setDebitCard(debitCard2);
        user2.setHasCurrentAccount(true);
        user2.setHasSavingsAccount(true);
        userRepository.save(user2);
        accountRepository.save(account3);

        DebitCard debitCard1 = new DebitCard("1111222233334444",1, LocalDate.now().plusYears(3), true, "UUID1", account1);
        account1.setHasCard(true);
        account1.setCardNumber(debitCard1.getCardNumber());
        account1.setCardPublicNumber(debitCard1.getPublicNumber());
        accountRepository.save(account1);
        debitCardRepository.save(debitCard1);

        DebitCard debitCard2 = new DebitCard("5555666677778888", 1, LocalDate.now().plusYears(2), true, "UUID2", account2);
        account2.setHasCard(true);
        account2.setCardNumber(debitCard2.getCardNumber());
        account2.setCardPublicNumber(debitCard2.getPublicNumber());
        accountRepository.save(account2);
        debitCardRepository.save(debitCard2);*/
    }

    @When("I request to get accounts with skip {int} and limit {int}")
    public void whenIRequestToGetAccountsWithSkipAndLimit(int skip, int limit) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + _tokenDTO.token());

        // Create the HTTP entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            response = restTemplate.exchange(
                    baseURL + "/accounts?skip=" + skip + "&limit=" + limit,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<AccountDTO>>() {}
            );
        } catch (HttpClientErrorException ex) {
            response = ResponseEntity.status(ex.getRawStatusCode()).build();
        }
    }

    @Then("I should receive a list of accounts")
    public void thenIShouldReceiveAListOfAccountsWithSize() {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AccountDTO> accounts = response.getBody();
        assertNotNull(accounts);
    }

    @Then("I should receive a bad request response")
    public void thenIShouldReceiveABadRequestResponse() {
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Given("I am logged in as an account admin")
    public void givenIAmLoggedInAsAnAdminAccount() {
        String username = "john.doe@gmail.com";
        String password = "123456";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);

        RestTemplate restTemplate = new RestTemplate();

        // Set the login endpoint URL
        String loginUrl = "http://localhost:8080/users/login";

        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the JSON payload for login
        String jsonPayload = "{\"username\": \"john.doe@gmail.com\", \"password\": \"123456\"}";

        // Create the HTTP entity with request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

        // Send the POST request to login
        ResponseEntity<TokenDTO> response = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        // Check the response status and perform further actions if needed
        if (response.getStatusCode().is2xxSuccessful()) {
            TokenDTO tokenDTO = response.getBody();

            _tokenDTO = tokenDTO;
            // Successfully logged in
            // Handle the tokenDTO or perform other actions
        }

    }

    @When("^I provide invalid account data$")
    public void iProvideInvalidAccountData() {
        NewAccountDTO newAccountDTO = new NewAccountDTO();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + _tokenDTO.token());

        // Create the HTTP entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            responseSingleAccountDTO = restTemplate.exchange(
                    baseURL + "/accounts",
                    HttpMethod.POST,
                    requestEntity,
                    AccountDTO.class,
                    newAccountDTO
            );
        } catch (HttpClientErrorException ex) {
            responseSingleAccountDTO = ResponseEntity.status(ex.getRawStatusCode()).build();
        }
    }

    @Then("the account creation should fail with error code {int}")
    public void theAccountCreationShouldFailWithErrorCode(int arg0) {
        switch (arg0){
            case 400:
                assertTrue(responseSingleAccountDTO.getStatusCode().is4xxClientError());
                break;
            default:
                fail("Unexpected error code");
        }
    }

    @When("I provide valid account data")
    public void iProvideValidAccountData() {
        ObjectMapper objectMapper = new ObjectMapper();

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        String password = bCryptPasswordEncoder.encode("123456");
        user1.setPassword(password);
        user1.setPhoneNumber("0612345678");
        user1.setHasCurrentAccount(false);
        user1.setHasSavingsAccount(false);
        user1.setRoles(List.of(Role.ROLE_ADMIN));
        Account account = new Account("NL01INHO9501054666",user1,"Current", "1234", 200.0, 1000.0, 0,100);
        NewAccountDTO newAccountDTO = new NewAccountDTO(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + _tokenDTO.token());
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Create the HTTP entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);



        try {
            responseSingleAccountDTO = restTemplate.exchange(
                    baseURL + "/accounts",
                    HttpMethod.POST,
                    requestEntity,
                    AccountDTO.class,
                    objectMapper.writeValueAsString(newAccountDTO)
            );
        } catch (HttpClientErrorException ex) {
            responseSingleAccountDTO = ResponseEntity.status(ex.getRawStatusCode()).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @And("the response should contain the created account details and the status code should be {int}")
    public void theResponseShouldContainTheCreatedAccountDetailsAndTheStatusCodeShouldBe(int arg0) {
        switch (arg0){
            case 201:
                assertTrue(responseSingleAccountDTO.getStatusCode().is2xxSuccessful());
                break;
            default:
                fail("Unexpected error code");
        }
    }
}