package Group6.BankingApp.Configuration;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.Services.AccountService;
import jakarta.persistence.Entity;
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
    private TransactionRepository transactionRrepository;

    @Autowired
    public MyApplicationRunner(UserRepository userRepository, AccountRepository accountRepository, DebitCardRepository debitCardRepository, TransactionRepository transactionRrepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
        this.transactionRrepository = transactionRrepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<User> users=
                Arrays.asList(
                        new User("John", "Doe", "john.doe@gmail.com", "123456", "123456789", Role.CUSTOMER,true),
                        new User("Jane", "Doe", "jane.doe@gmail.com", "123456", "123456789", Role.CUSTOMER,false),
                        new User("Adam", "Adey", "ad.ad@gmail.com", "123456", "123456789", Role.CUSTOMER,false)
                        );

        userRepository.saveAll(users);

        // Create and save DebitCards
        DebitCard debitCard1 = new DebitCard("1111222233334444", LocalDate.now().plusYears(3), "123", "John Doe", true, "UUID1");
        debitCardRepository.save(debitCard1);
        DebitCard debitCard2 = new DebitCard("5555666677778888", LocalDate.now().plusYears(2), "456", "Jane Smith", true, "UUID2");
        debitCardRepository.save(debitCard2);

        // Create and save Accounts
        Account account1 = new Account("NL01INHO9501054837","Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account1.setDebitCard(debitCard1);
        account1.setUser(users.get(0));
        accountRepository.save(account1);
        Account account2 = new Account("NL01INHO2371458805", "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
        account2.setDebitCard(debitCard2);
        account2.setUser(users.get(1));
        accountRepository.save(account2);
        Account account3 = new Account("NL01INHO5808504708", "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account3.setDebitCard(debitCard1);
        account3.setUser(users.get(2));
        accountRepository.save(account3);
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
        transactionRrepository.save(transaction1);
        Transaction transaction2 = new Transaction("NL67INGB12345678", "NL67INGB87654321", 155.0, "deposit");
        transactionRrepository.save(transaction2);
        Transaction transaction3 = new Transaction("NL67INGB12348765", "NL67INGB87655555", 100.0, "regular");
        transactionRrepository.save(transaction3);
    }
}
