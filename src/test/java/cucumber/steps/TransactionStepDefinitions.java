package cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Services.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TransactionStepDefinitions extends BaseStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    private ResponseEntity<String> response;

    @Autowired
    private ObjectMapper mapper;

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailable(String endpoint, String method) {
        response = restTemplate.exchange(
                "/" + endpoint,
                HttpMethod.OPTIONS,
                new HttpEntity<>(null, httpHeaders), // null because OPTIONS does not have a body
                String.class);

        List<String> options = Arrays.asList(response.getHeaders()
                .get("Allow")
                .get(0) // The first element is all allowed methods separated by comma
                .split(","));

        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        response = restTemplate.exchange(
                restTemplate.getRootUri() + "/transactions",
                HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                String.class);
    }

    @Then("I should receive all transactions")
    public void iShouldReceiveAllTransactions() {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(1, actual);
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
        dto.setDescription(description);
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
