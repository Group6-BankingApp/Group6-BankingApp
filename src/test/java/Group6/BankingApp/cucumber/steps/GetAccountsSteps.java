package Group6.BankingApp.cucumber.steps;

import Group6.BankingApp.Models.dto.AccountDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountsSteps {

    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<List<AccountDTO>> response;
    private final String baseURL = "http://localhost:8080"; // Replace with your API's base URL

    @Given("there are accounts in the system")
    public void givenThereAreAccountsInTheSystem() {
        // Implement setup or data preparation logic here
    }

    @When("I request to get accounts with skip {int} and limit {int}")
    public void whenIRequestToGetAccountsWithSkipAndLimit(int skip, int limit) {
        try {
            response = restTemplate.exchange(
                    baseURL + "/accounts?skip=" + skip + "&limit=" + limit,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AccountDTO>>() {}
            );
        } catch (HttpClientErrorException ex) {
            response = ResponseEntity.status(ex.getRawStatusCode()).build();
        }
    }

    @Then("I should receive a list of accounts with size {int}")
    public void thenIShouldReceiveAListOfAccountsWithSize(int expectedSize) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AccountDTO> accounts = response.getBody();
        assertNotNull(accounts);
        assertEquals(expectedSize, accounts.size());
    }

    @Then("I should receive a bad request response")
    public void thenIShouldReceiveABadRequestResponse() {
        assertTrue(response.getStatusCode().is4xxClientError());
    }
}