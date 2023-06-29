package Group6.BankingApp.Controllers;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.AtmResponseDTO;
import Group6.BankingApp.Models.dto.AtmTransactionDTO;
import Group6.BankingApp.Models.dto.FilterDTO;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Services.TransactionService;

import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TransactionControllerTest {
    
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService);
    }


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
    @Test
    void testGetAllTransactions() {
        // Arrange
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(),
                new Transaction()
        );
        when(transactionService.findAll()).thenReturn(mockTransactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransactions, response.getBody());
        verify(transactionService).findAll();
    }
    @Test
    void testGetFilteredTransactionsByIban() {
        // Arrange
        String mockIban = "mock-iban";
        FilterDTO mockFilter = new FilterDTO();
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction(),
                new Transaction()
        );
        when(transactionService.findTransactionsByFilter(mockIban, mockFilter)).thenReturn(mockTransactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getFilteredTransactionsByIban(mockIban, mockFilter);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransactions, response.getBody());
        verify(transactionService).findTransactionsByFilter(mockIban, mockFilter);
    }
    
    @Test
    void testDeposit() {
        // Arrange
        AtmTransactionDTO mockTransaction = new AtmTransactionDTO();
        AtmResponseDTO mockResponseDTO = new AtmResponseDTO();
        when(transactionService.makeDeposit(mockTransaction)).thenReturn(mockResponseDTO);

        // Act
        ResponseEntity<AtmResponseDTO> response = transactionController.Deposit(mockTransaction);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponseDTO, response.getBody());
        verify(transactionService).makeDeposit(mockTransaction);
    }
    @Test
    void testWithdraw() {
    // Arrange
        AtmTransactionDTO mockTransaction = new AtmTransactionDTO();
        AtmResponseDTO mockResponseDTO = new AtmResponseDTO();
        when(transactionService.makeWithdraw(mockTransaction)).thenReturn(mockResponseDTO);

        // Act
        ResponseEntity<AtmResponseDTO> response = transactionController.Withdraw(mockTransaction);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponseDTO, response.getBody());
        verify(transactionService).makeWithdraw(mockTransaction);
    }
    @Test
    void testMakeTransfer() {
        // Arrange
        TransactionDTO mockTransactionDTO = new TransactionDTO();
        Transaction mockTransaction = new Transaction();
        when(transactionService.addTransfer(mockTransactionDTO)).thenReturn(mockTransaction);

        // Act
        ResponseEntity response = transactionController.makeTransfer(mockTransactionDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockTransaction, response.getBody());
        verify(transactionService).addTransfer(mockTransactionDTO);
    }
}
