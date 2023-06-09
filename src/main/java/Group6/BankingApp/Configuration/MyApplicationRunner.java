package Group6.BankingApp.Configuration;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.Services.AccountService;
import jakarta.persistence.Entity;

import org.hibernate.dialect.function.TransactSQLStrFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
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

        List<User> users=
                Arrays.asList(new User("InHolland", "Bank", "inholland@gmail.com", "123456", "123456789", Role.EMPLOYEE,true),
                        new User("John", "Doe", "john.doe@gmail.com", "123456", "123456789", Role.EMPLOYEE,true),
                        new User("Emily", "Doe", "emily.doe@gmail.com", "123456", "123456789", Role.CUSTOMER,false),
                        new User("Adam", "Adey", "ad.ad@gmail.com", "123456", "123456789", Role.CUSTOMER,false)
                        );

        userRepository.saveAll(users);


        // Create and save DebitCards
        DebitCard bankDebitCard = new DebitCard("1111222233334444",
                LocalDate.now().plusYears(3),
                "123",
                users.get(0).getFirstName() + " "  + users.get(0).getLastName(),
                true,
                accountService.generateCardUUID());
        debitCardRepository.save(bankDebitCard);
        DebitCard debitCard1 = new DebitCard("5555666677778888",
                LocalDate.now().plusYears(2),
                "456",
                users.get(1).getFirstName() + " "  + users.get(1).getLastName(),
                true,
                accountService.generateCardUUID());
        debitCardRepository.save(debitCard1);
        DebitCard debitCard2 = new DebitCard("5555666677778888",
                LocalDate.now().plusYears(2),
                "456",
                users.get(2).getFirstName() + " "  + users.get(2).getLastName(),
                true,
                accountService.generateCardUUID());
        debitCardRepository.save(debitCard2);
        DebitCard debitCard3 = new DebitCard("6666777788889999",
                LocalDate.now().plusYears(10),
                "789",
                users.get(3).getFirstName() + " "  + users.get(3).getLastName(),
                true,
                accountService.generateCardUUID());
        debitCardRepository.save(debitCard3);

        // Create and save Accounts
        Account inHollandAccount = new Account("NL01INHO0000000001","Current", accountService.generateCardUUID(), "1234", 1000000.0, 1000000.0, 1000000, true, bankDebitCard);
        inHollandAccount.setUser(users.get(0));
        accountRepository.save(inHollandAccount);
        Account account1 = new Account("NL01INHO9501054837","Savings", accountService.generateCardUUID(), "1234", 1000.0, 600.0, 0, true, debitCard1);
        account1.setUser(users.get(1));
        accountRepository.save(account1);
        Account account2 = new Account("NL01INHO2371458805", "Current", accountService.generateCardUUID(), "5678", 2000.0, 950.0, 0, true, debitCard2);
        account2.setUser(users.get(2));
        accountRepository.save(account2);
        Account account3 = new Account("NL01INH1234567890", "Current", accountService.generateCardUUID(), "5678", 2000.0, 950.0, -100, true, debitCard2);
        account3.setUser(users.get(3));
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
            Transaction transaction1 = new Transaction("NL67INGB2131241242", "NL67INGB213214124", 200.0, "withdraw");
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction("NL67INGB12345678", "NL67INGB87654321", 155.0, "deposit");
            transactionRepository.save(transaction2);
            Transaction transaction3 = new Transaction("NL67INGB12348765", "NL67INGB87655555", 100.0, "regular");
            transactionRepository.save(transaction3);
    }
}
