package Group6.BankingApp.cucumber.steps;

import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Services.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class TransactionStepDefinitions {

    TokenDTO _tokenDTO;

    private final String baseURL = "http://localhost:8080";

    private TransactionService transactionService;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    private ResponseEntity<String> response;

    private final RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper mapper;

    @Given("The endpoint for {string} is available for method {string}")
    public boolean theEndpointForIsAvailable(String endpoint, String method) {

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

    @Given("I am logged in as an admin")
    public void givenIAmLoggedInAsAnAdmin() {
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

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + _tokenDTO.token());

        response = restTemplate.exchange(
                baseURL + "/transactions",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                String.class);
    }

    @Then("I should receive all transactions")
    public void iShouldReceiveAllTransactions() {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertNotNull(actual);
    }

    @When("I create a transaction with sender {string}, receiver {string}, amount {double}, and description {string}")
    public void iCreateATransactionWithSenderReceiverAmountAndDescription(String sender, String receiver, double amount, String description) throws JsonProcessingException {
        TransactionDTO dto = createTransactionDto(sender, receiver, amount, description);
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange(
                "/transactions",
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders),
                String.class);
    }

    private TransactionDTO createTransactionDto(String sender, String receiver, double amount, String description) {
        TransactionDTO dto = new TransactionDTO();
        dto.setSenderIban(sender);
        dto.setReceiverIban(receiver);
        dto.setAmount(amount);
        return dto;
    }

    @Then("The response status is {int}")
    public void theResponseStatusIs(int status) {
        Assertions.assertEquals(status, response.getStatusCode().value());
    }

    @And("The transaction ID is {string}")
    public void theTransactionIdIs(String transactionId) throws JsonProcessingException {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        Assertions.assertNotNull(transaction);
    }
}
