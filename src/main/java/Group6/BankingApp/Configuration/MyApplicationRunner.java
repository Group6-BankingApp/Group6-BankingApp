package Group6.BankingApp.Configuration;

import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import jakarta.persistence.Entity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
public class MyApplicationRunner implements ApplicationRunner {

    private UserRepository userRepository;

    public MyApplicationRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<User> users=
                Arrays.asList(
                        new User("John", "Doe", "john.doe@gmail.com", "123456", "123456789", Role.CUSTOMER));

        users.forEach(user -> userRepository.save(user));
    }
}
