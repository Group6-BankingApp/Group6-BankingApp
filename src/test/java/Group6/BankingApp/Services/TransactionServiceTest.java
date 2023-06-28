package Group6.BankingApp.Services;
import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.Services.AccountService;

import org.hibernate.service.spi.ServiceException;
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
    @Test
    public void testAddTransfer() {

        float originalBalance = 1000;
        //Create users to send and receive money
        User sendUser = new User();
        sendUser.setFirstName("John");
        sendUser.setLastName("Doe");
        sendUser.setEmail("john.doe@gmail.com");
        sendUser.setPassword("password");
        sendUser.setPhoneNumber("0612345678");
        sendUser.setHasCurrentAccount(false);
        sendUser.setHasSavingsAccount(false);
        sendUser.setRoles(List.of(Role.ROLE_ADMIN));

        User receiverUser = new User();
        receiverUser.setFirstName("John");
        receiverUser.setLastName("Doe");
        receiverUser.setEmail("john.doe@gmail.com");
        receiverUser.setPassword("password");
        receiverUser.setPhoneNumber("0612345678");
        receiverUser.setHasCurrentAccount(false);
        receiverUser.setHasSavingsAccount(false);
        receiverUser.setRoles(List.of(Role.ROLE_ADMIN));

        // Set up test data
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSenderIban("NL01INHO9501054837");
        transactionDTO.setReceiverIban("NL01INHO9501054123");
        transactionDTO.setAmount(100.0);
        UserDTO2 userDTO2 = new UserDTO2(receiverUser);
        userDTO2.setId(1L);
        transactionDTO.setUserDTO2(userDTO2);
        
        Account senderAccount = new Account("NL01INHO9501054837",sendUser,"Current", "1234", 200.0, originalBalance, 0,1000);
        when(accountService.findAccountByIban(transactionDTO.getSenderIban())).thenReturn(senderAccount);
        
        Account receiverAccount = new Account("NL01INHO9501054123",receiverUser,"Current", "1234", 200.0, originalBalance, 0,1000);
        when(accountService.findAccountByIban(transactionDTO.getReceiverIban())).thenReturn(receiverAccount);
        
        //run method
        Transaction result = transactionService.addTransfer(transactionDTO);

        // Assertions

        assertEquals(originalBalance - transactionDTO.getAmount(), senderAccount.getBalance());
        assertEquals(originalBalance + transactionDTO.getAmount(), receiverAccount.getBalance());

        verify(accountService, times(1)).findAccountByIban(transactionDTO.getSenderIban());
        verify(accountService, times(1)).findAccountByIban(transactionDTO.getReceiverIban());
        verify(accountService, times(1)).updateAccountByIban(senderAccount.getIban(), senderAccount);
        verify(accountService, times(1)).updateAccountByIban(receiverAccount.getIban(), receiverAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testAddTransfer_InsufficientBalance() {

        float originalBalance = 10;
        //Create users to send and receive money
        User sendUser = new User();
        sendUser.setFirstName("John");
        sendUser.setLastName("Doe");
        sendUser.setEmail("john.doe@gmail.com");
        sendUser.setPassword("password");
        sendUser.setPhoneNumber("0612345678");
        sendUser.setHasCurrentAccount(false);
        sendUser.setHasSavingsAccount(false);
        sendUser.setRoles(List.of(Role.ROLE_ADMIN));

        User receiverUser = new User();
        receiverUser.setFirstName("John");
        receiverUser.setLastName("Doe");
        receiverUser.setEmail("john.doe@gmail.com");
        receiverUser.setPassword("password");
        receiverUser.setPhoneNumber("0612345678");
        receiverUser.setHasCurrentAccount(false);
        receiverUser.setHasSavingsAccount(false);
        receiverUser.setRoles(List.of(Role.ROLE_ADMIN));

        // Set up test data
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSenderIban("NL01INHO9501054837");
        transactionDTO.setReceiverIban("NL01INHO9501054123");
        transactionDTO.setAmount(100.0);
        UserDTO2 userDTO2 = new UserDTO2(receiverUser);
        userDTO2.setId(1L);
        transactionDTO.setUserDTO2(userDTO2);
        
        Account senderAccount = new Account("NL01INHO9501054837",sendUser,"Current", "1234", 200.0, originalBalance, 0,1000);
        when(accountService.findAccountByIban(transactionDTO.getSenderIban())).thenReturn(senderAccount);
        
        Account receiverAccount = new Account("NL01INHO9501054123",receiverUser,"Current", "1234", 200.0, originalBalance, 0,1000);
        when(accountService.findAccountByIban(transactionDTO.getReceiverIban())).thenReturn(receiverAccount);
        
        //run method
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            transactionService.addTransfer(transactionDTO);
        });
        
        // Assert the specific error message
        String expectedErrorMessage = "Failed to add transaction. ";
        String actualErrorMessage = exception.getMessage();
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }

}
