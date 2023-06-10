package Group6.BankingApp.Controllers;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {
    
    @Mock
    private TransactionService transactionService;
    
    private TransactionController transactionController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService);
    }
    
    @Test
    void testGetAllTransactions() {
        // Mock the dependencies
        String iban = "";
        Integer skip = 0;
        Integer limit = 40;
        String dateFrom = "";
        String dateTo = "";
        String pin = "";
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionService.findAllTransactions(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(transactions);
                
    }
    
    @Test
    void testGetAllTransactions_WithInvalidSkipParameter() {
        // Call the method with invalid skip parameter
        ResponseEntity<List<Transaction>> responseEntity = transactionController.getAllTransactions("" , -1, 40, "", "", "");
        
        // Verify that it returns a bad request response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    void testGetAllTransactions_WithInternalServerError() {
        // Mock the service to throw an exception
        when(transactionService.findAllTransactions(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Internal Server Error"));
        
        // Call the method being tested
        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () -> {
            transactionController.getAllTransactions("", 0, 40, "", "", "");
        });
        
        // Verify that it throws a response status exception with the correct status and message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to retrieve transactions", exception.getReason());
    }
    
}
