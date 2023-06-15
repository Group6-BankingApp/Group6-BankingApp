package Group6.BankingApp.Services;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTransactions() {
        // Create a list of transactions
        List<Transaction> transactions = Arrays.asList(
                new Transaction("sender1", "receiver1", 100.0),
                new Transaction("sender2", "receiver2", 200.0)
        );

        // Mock the behavior of the transaction repository to return the list of transactions
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Call the getAllTransactions() method on the transaction service
        List<Transaction> result = transactionService.getAllTransactions();

        // Verify that the transaction repository method was called
        verify(transactionRepository, times(1)).findAll();

        // Verify that the returned list is not null and has the correct size
        assertNotNull(result);
        assertEquals(2, result.size());
        // You can perform additional assertions on the returned list if needed

        // You can also assert the specific contents of the returned list if needed
        assertEquals(transactions.get(0), result.get(0));
        assertEquals(transactions.get(1), result.get(1));
    }

    @Test
    void testGetTransactionById() {
        // Create a transaction with a specific ID
        String transactionId = "transaction1";
        Transaction transaction = new Transaction("sender1", "receiver1", 100.0);
        transaction.setTransactionId(transactionId);

        // Mock the behavior of the transaction repository to return the transaction with the specified ID
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        // Call the getTransactionById() method on the transaction service
        Transaction result = transactionService.getTransactionById(transactionId);

        // Verify that the transaction repository method was called
        verify(transactionRepository, times(1)).findById(transactionId);

        // Verify that the returned transaction is not null and has the correct ID
        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        // You can perform additional assertions on the returned transaction if needed

        // You can also assert the specific contents of the returned transaction if needed
        assertEquals(transaction.getSenderIban(), result.getSenderIban());
        assertEquals(transaction.getReceiverIban(), result.getReceiverIban());
        assertEquals(transaction.getAmount(), result.getAmount());
    }


}
