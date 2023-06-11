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
        DebitCard bankDebitCard = new DebitCard("5538545966347381", LocalDate.now().plusYears(3), true, accountService.generateCardUUID());
        debitCardRepository.save(bankDebitCard);
        DebitCard debitCard1 = new DebitCard("5420508641550416", LocalDate.now().plusYears(2), true, accountService.generateCardUUID());
        debitCardRepository.save(debitCard1);
        DebitCard debitCard2 = new DebitCard("5118356014254647", LocalDate.now().plusYears(2), true, accountService.generateCardUUID());
        debitCardRepository.save(debitCard2);
        DebitCard debitCard3 = new DebitCard("5341778909423491", LocalDate.now().plusYears(10), true, accountService.generateCardUUID());
        debitCardRepository.save(debitCard3);

        // Create and save Accounts
        Account inHollandAccount = new Account("NL01INHO0000000001","Current", accountService.generateCardUUID(), "1234", 1000000.0, 1000000.0, 1000000, true, bankDebitCard.getCardNumber());
        inHollandAccount.setUser(users.get(0));
        accountRepository.save(inHollandAccount);
        Account account1 = new Account("NL01INHO9501054837","Savings", accountService.generateCardUUID(), "1234", 1000.0, 600.0, 0, true, null);
        account1.setUser(users.get(1));
        accountRepository.save(account1);
        Account account2 = new Account("NL01INHO2371458805", "Current", accountService.generateCardUUID(), "5678", 2000.0, 950.0, 0, true, null);
        account2.setUser(users.get(2));
        accountRepository.save(account2);
        Account account3 = new Account("NL01INH1234567890", "Current", accountService.generateCardUUID(), "5678", 2000.0, 950.0, -100, true, null);
        account3.setUser(users.get(3));
        accountRepository.save(account3);

            // Create and save Transactions
            Transaction transaction1 = new Transaction("NL67INGB2131241242", "NL67INGB213214124", 200.0, "withdraw");
            transactionRepository.save(transaction1);
            Transaction transaction2 = new Transaction("NL67INGB12345678", "NL67INGB87654321", 155.0, "deposit");
            transactionRepository.save(transaction2);
            Transaction transaction3 = new Transaction("NL67INGB12348765", "NL67INGB87655555", 100.0, "regular");
            transactionRepository.save(transaction3);
    }
}
