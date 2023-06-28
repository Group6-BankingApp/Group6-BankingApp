package Group6.BankingApp.Services;
import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import Group6.BankingApp.Models.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    @Autowired
    private AccountService accountService;

    @Mock
    private UserService userService;

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
//     @Test
//     public void testAddTransfer() {
//         // Mock the necessary dependencies
//         AccountService accountServiceMock = mock(AccountService.class);
//         AccountRepository accountRepository = mock(AccountRepository.class);
//         TransactionRepository transactionRepositoryMock = mock(TransactionRepository.class);
//         // Crreate Users
//         User user1 = new User();
//         user1.setFirstName("John");
//         user1.setLastName("Doe");
//         user1.setEmail("john.doe@gmail.com");
//         user1.setPassword("password");
//         user1.setPhoneNumber("0612345678");
//         user1.setHasCurrentAccount(false);
//         user1.setHasSavingsAccount(false);
//         user1.setRoles(List.of(Role.ROLE_ADMIN));

//         User user2 = new User();
//         user2.setFirstName("Jane");
//         user2.setLastName("Smith");
//         user2.setEmail("jane.smith@gmail.com");
//         user2.setPassword("password2");
//         user2.setPhoneNumber("0612345678");
//         user2.setHasCurrentAccount(false);
//         user2.setHasSavingsAccount(false);
//         user2.setRoles(List.of(Role.ROLE_ADMIN));

//         List<User> users = Arrays.asList(user1, user2);
//         userRepository.saveAll(users);

//         //Create accounts
//         Account account1 = new Account("NL01INHO9501054837",user1,"Current", "1234", 200.0, 1000.0, 0,100);
//         Account account2 = new Account("NL01INHO9501054804",user1,"Current", "1234", 1000.0, 1000.0, 0, 100);
//         accountRepository.save(account1);
//         accountRepository.save(account2);
//         // Create a sample transactionDTO
//         TransactionDTO transactionDTO = new TransactionDTO();
//         transactionDTO.setSenderIban("NL01INHO9501054804");
//         transactionDTO.setReceiverIban("NL01INHO9501054837");
//         transactionDTO.setAmount(100.0);

//         // Perform the test
//         Transaction result = transactionService.addTransfer(transactionDTO);

//         // Assert the result or perform any necessary verifications
//         assertNotNull(result);
//         // Add additional assertions based on your requirements
// }


}
