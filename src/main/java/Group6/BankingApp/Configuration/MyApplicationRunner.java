package Group6.BankingApp.Configuration;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
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

    @Autowired
    public MyApplicationRunner(UserRepository userRepository, AccountRepository accountRepository, DebitCardRepository debitCardRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<User> users=
                Arrays.asList(
                        new User("John", "Doe", "john.doe@gmail.com", "123456", "123456789", Role.EMPLOYEE,true),
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
        Account account1 = new Account("NL01INHO9501054837", users.get(2), "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account1.setDebitCard(debitCard1);
        accountRepository.save(account1);
        Account account2 = new Account("NL01INHO2371458805", users.get(1), "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
        account2.setDebitCard(debitCard2);
        accountRepository.save(account2);
        Account account3 = new Account("NL01INHO5808504708", users.get(2), "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account3.setDebitCard(debitCard1);
        accountRepository.save(account3);
        Account account4 = new Account("NL01INHO8399675482", users.get(1), "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
        account4.setDebitCard(debitCard2);
        accountRepository.save(account4);
        Account account5 = new Account("NL01INHO8135680120", users.get(2), "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account5.setDebitCard(debitCard1);
        accountRepository.save(account5);
        Account account6 = new Account("NL01INHO4789170233", users.get(1), "Current", null, null, 2000.0, 0.0, 10000.0, true, null);
        account6.setDebitCard(debitCard2);
        accountRepository.save(account6);
    }
}
