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
                        new User("John", "Doe", "john.doe@gmail.com", "123456", "123456789", Role.CUSTOMER),
                        new User("Jane", "Doe", "jane.doe@gmail.com", "123456", "123456789", Role.CUSTOMER),
                        new User("Adam", "Addey", "ad.ad@gmail.com", "123456", "123456789", Role.CUSTOMER)
                        );

        userRepository.saveAll(users);

        // Create and save DebitCards
        DebitCard debitCard1 = new DebitCard("1111222233334444", LocalDate.now().plusYears(3), "123", "John Doe", true, "UUID1");
        debitCardRepository.save(debitCard1);
        DebitCard debitCard2 = new DebitCard("5555666677778888", LocalDate.now().plusYears(2), "456", "Jane Smith", true, "UUID2");
        debitCardRepository.save(debitCard2);

        // Create and save Accounts
        Account account1 = new Account("IBAN1", users.get(2), "Savings", null, null, 1000.0, 0.0, 5000.0, true, null);
        account1.setDebitCard(debitCard1); // Associate debitCard1 with account1
        accountRepository.save(account1);
        Account account2 = new Account("IBAN2", users.get(1), "Savings", null, null, 2000.0, 0.0, 10000.0, true, null);
        account2.setDebitCard(debitCard2); // Associate debitCard2 with account2
        accountRepository.save(account2);
    }
}
