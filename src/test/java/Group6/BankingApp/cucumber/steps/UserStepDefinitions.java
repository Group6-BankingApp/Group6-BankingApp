package Group6.BankingApp.cucumber.steps;

import Group6.BankingApp.Controllers.UserController;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.TokenDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class UserStepDefinitions {

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
        String username = "mohamad@gmail.com";
        String password = "123456";

        // Prepare the login request body
        LoginDTO loginDTO = new LoginDTO(username, password);

        // Set the login endpoint URL
        String loginUrl = "http://localhost:8080/login";

        // Prepare the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the HTTP entity with request body and headers
        HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);

        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request to login
        ResponseEntity<TokenDTO> response = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, TokenDTO.class);

        // Check the response status and perform further actions if needed
        if (response.getStatusCode().is2xxSuccessful()) {
            TokenDTO tokenDTO = response.getBody();
            assertNotNull(tokenDTO);
            // Successfully logged in
            // Handle the tokenDTO or perform other actions
        }
    }

    @Then("the response should contain a token")
    public void verifyResponseContainsToken() {
        // Assert that the response contains a token
        // Example assertion:
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof TokenDTO);
        assertNotNull((responseEntity.getBody()));
    }

    @When("I request to get all users with associated accounts")
    public void requestAllUsersWithAccounts() {
        responseEntity = userController.getAllUsersWithAccount();
    }

    @Then("the response should contain a list of users with their account details")
    public void verifyResponseContainsUsersWithAccounts() {
        // Assert that the response contains a list of users with their account details
        // Example assertion:
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof List<?>);
        List<?> usersWithAccounts = (List<?>) responseEntity.getBody();
        // Perform additional assertions as per your application's response structure
    }

    @When("I request to get all users without associated accounts")
    public void requestAllUsersWithoutAccounts() {
        responseEntity = userController.getAllUsersWithoutAccount();
    }

    @Then("the response should contain a list of users without any account details")
    public void verifyResponseContainsUsersWithoutAccounts() {
        // Assert that the response contains a list of users without any account details
        // Example assertion:
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof List<?>);
        List<?> usersWithoutAccounts = (List<?>) responseEntity.getBody();
        // Perform additional assertions as per your application's response structure
    }

    @Given("I have admin access")
    public void haveAdminAccess() {
        // No action needed for this step definition as it is a setup step
    }

    @When("I request to get all users")
    public void requestAllUsers() {
        responseEntity = userController.getAllUsers();
    }

}