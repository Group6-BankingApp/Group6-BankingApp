package Group6.BankingApp.cucumber.steps;

import Group6.BankingApp.Controllers.UserController;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.TokenDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class UserStepDefinitions {

    TokenDTO _tokenDTO;

    private RestTemplate restTemplate = new RestTemplate();

    // Inject your controllers and repositories here
    private UserController userController;

    private ResponseEntity<?> responseEntity;
    private Long userId;

    // Step Definitions for the scenarios

    @Given("the endpoint for {string} is available for method {string}")
    public boolean theEndpointIsAvailable(String endpoint, String method) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Send a GET request to the endpoint
            ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);

            // Check the response status code
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpStatusCodeException e) {
            // Handle specific HTTP status code exceptions if needed
            return false;
        } catch (Exception e) {
            // Handle general exceptions if needed
            return false;
        }
    }

    @When("I make a login request with valid credentials")
    public void makeLoginRequestWithValidCredentials() {
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
        }

        assertNotNull(_tokenDTO);
    }

    @Then("the response should contain a token")
    public void verifyResponseContainsToken() {
        // Assert that the response contains a token
        assertNotNull(_tokenDTO);
    }

    @When("I request to get all users with associated accounts")
    public void requestAllUsersWithAccounts() {
        String url = "http://localhost:8080/users/withAccount";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + _tokenDTO.token());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        } catch (HttpClientErrorException ex) {
            responseEntity = new ResponseEntity<>(ex.getStatusCode());
        }
    }

    @Then("the response should contain a list of users with their account details")
    public void verifyResponseContainsUsersWithAccounts() {
        // Assert that the response contains a list of users with their account details
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof List<?>);
        List<?> usersWithAccounts = (List<?>) responseEntity.getBody();
    }

    @When("I request to get all users without associated accounts")
    public void requestAllUsersWithoutAccounts() {
        responseEntity = userController.getAllUsersWithoutAccount();
    }

    @Then("the response should contain a list of users without any account details")
    public void verifyResponseContainsUsersWithoutAccounts() {
        // Assert that the response contains a list of users without any account details
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof List<?>);
        List<?> usersWithoutAccounts = (List<?>) responseEntity.getBody();
    }

    @Given("I have admin access")
    public void haveAdminAccess() {
    }

    @When("I request to get all users")
    public void requestAllUsers() {
        responseEntity = userController.getAllUsers();
    }

}