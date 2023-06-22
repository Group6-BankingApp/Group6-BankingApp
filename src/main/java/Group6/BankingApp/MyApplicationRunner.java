package Group6.BankingApp;

import Group6.BankingApp.Controllers.AccountController;
import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.*;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Services.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private DebitCardRepository debitCardRepository;
    private AccountService accountService;
    private TransactionRepository transactionRepository;

    @Autowired
    public MyApplicationRunner(UserRepository userRepository, AccountRepository accountRepository, DebitCardRepository debitCardRepository, AccountService accountService, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Create and save Customers
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        user1.setPassword("123456");
        user1.setPhoneNumber("0612345678");
        user1.setHasAccount(true);
        user1.setRoles(List.of(Role.ROLE_ADMIN));

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@gmail.com");
        user2.setPassword("123456");
        user2.setPhoneNumber("0612345678");
        user2.setHasAccount(true);
        user2.setRoles(List.of(Role.ROLE_USER));

        List<User> users = Arrays.asList(user1, user2);
        userRepository.saveAll(users);

        // Create and save DebitCards
        DebitCard debitCard1 = new DebitCard("1111222233334444", LocalDate.now().plusYears(3), "123", "John Doe", true, "UUID1");
        debitCardRepository.save(debitCard1);
        DebitCard debitCard2 = new DebitCard("5555666677778888", LocalDate.now().plusYears(2), "456", "Jane Smith", true, "UUID2");
        debitCardRepository.save(debitCard2);

        // Create and save Accounts
        Account account1 = new Account("NL01INHO9501054837",user1,"Current", accountService.generateCardUUID(), "1234", 1000.0, 1000.0, 0,100, true, debitCard1);
        Account account= new Account("NL01INHO9501054804",user1,"Savings", accountService.generateCardUUID(), "1234", 1000.0, 1000.0, 0, 100,true, debitCard1);
        accountRepository.save(account1);
        accountRepository.save(account);
        Account account2 = new Account("NL01INHO2371458805",user2, "Current", accountService.generateCardUUID(), "5678", 2000.0, 950.0, 0,100, true, debitCard2);
        //account2.setDebitCard(debitCard2);
        accountRepository.save(account2);

        Account account3 = new Account("NL01INH1234567890",user2, "Savings", accountService.generateCardUUID(), "5678", 2000.0, 950.0, -100,100, true, debitCard2);
        //account2.setDebitCard(debitCard2);
        accountRepository.save(account3);
//        Account account3 = new Account("NL01INHO5808504708", "Savings", accountService.generateCardUUID(), "1357", 1000.0, 0.0, 5000.0, true, null);
//        account3.setDebitCard(debitCard1);
//        account3.setUser(users.get(2));
//        accountRepository.save(account3);
//        Account account4 = new Account("NL01INHO8399675482", "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
//        account4.setDebitCard(debitCard2);
//        account4.setUser(users.get(0));
//        accountRepository.save(account4);
//        Account account5 = new Account("NL01INHO8135680120", users.get(1), "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
//        account5.setDebitCard(debitCard1);
//        account5.setUser(users.get(1));
//        accountRepository.save(account5);
//        Account account6 = new Account("NL01INHO4789170233", users.get(0), "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
//        account6.setDebitCard(debitCard2);
//        account6.setUser(users.get(2));
//        accountRepository.save(account6);
            // Create and save Transactions
            Transaction transaction1 = new Transaction(account1.getIban(), account2.getIban(), 200.0);
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction(account2.getIban(), account3.getIban(), 155.0);
            transactionRepository.save(transaction2);
            Transaction transaction3 = new Transaction(account2.getIban(), account1.getIban(), 100.0);
            transactionRepository.save(transaction3);

            Iterable <Transaction> transactions =  transactionRepository.findAllBySenderIbanOrReceiverIban(account1.getIban(), account1.getIban());
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getAmount());
            }
    }
}
