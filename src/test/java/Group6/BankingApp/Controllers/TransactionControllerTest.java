package Group6.BankingApp.Controllers;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.TransactionService;

import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

//    @Test
//    void testGetAllTransactions() {
//        // Mocking the service response
//        List<Transaction> transactions = new ArrayList<>();
//        when(transactionService.findAllTransactions(0, 40, "", "", "NL67INGB1234567890", "")).thenReturn(transactions);
//
//        // Calling the controller method
//        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions("NL67INGB1234567890", 0, 40,  "","", "");
//
//        // Verifying the response
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(transactions, response.getBody());
//        verify(transactionService, times(1)).findAllTransactions(0, 40, (LocalDate.now().minusYears(1)).toString(), (LocalDate.now().toString()), "NL67INGB1234567890", "");
//    }

    @Test
    void testGetTransactionById() {
        String id = "123";
        Transaction transaction = new Transaction();
        when(transactionService.getTransactionById(id)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransactionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).getTransactionById(id);
    }


//    @Test
//    void testAddTransaction() {
//        Transaction transaction = new Transaction();
//        String pin = "0000";
//        Transaction newTransaction = new Transaction();
//
//        when(transactionService.addTransaction(transaction, pin)).thenReturn(newTransaction);
//
//        ResponseEntity response = transactionController.addTransaction(transaction, pin, "token");
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(newTransaction, response.getBody());
//        verify(transactionService, times(1)).addTransaction(transaction, pin);
//    }

}
