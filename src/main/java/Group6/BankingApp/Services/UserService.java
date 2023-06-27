package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.util.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider, AccountService accountService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.accountService = accountService;
    }

    public List<UserDTO2> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        List<UserDTO2>userDTO2s= convertToUserDTO2(users);
        return userDTO2s;
    }

    public List<UserDTO2> getAllUsersWithAccount() {
        List<User> users = (List<User>) userRepository.findAllByHasCurrentAccountIsTrueAndHasSavingsAccountIsTrue();
        List<User> users2 = (List<User>) userRepository.findAllByHasCurrentAccountIsTrueAndHasSavingsAccountIsFalse();
        List<User> users3 = (List<User>) userRepository.findAllByHasCurrentAccountIsFalseAndHasSavingsAccountIsTrue();
        users.addAll(users2);
        users.addAll(users3);
        List<UserDTO2>userDTO2s= convertToUserDTO2(users);
        return userDTO2s;
    }

    public UserDTO2 getUserById(Long id) {

        User user = userRepository.findById(id).orElse(null);
        if(user ==null){
            throw new EntityNotFoundException("User not found");
        }
        UserDTO2 userdto = new UserDTO2(user);
        return userdto;
    }

    public UserDTO2 addUser(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isEmpty()){
            String password = bCryptPasswordEncoder.encode(userDTO.getPassword());
            User user = new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(password);
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setRoles(List.of(Role.ROLE_USER));
            user.setHasCurrentAccount(false);
            user.setHasSavingsAccount(false);
            UserDTO2 userdto = new UserDTO2(userRepository.save(user));
            return userdto;
        }
       throw new IllegalArgumentException("Email is already taken");
    }

    public UserDTO2 updateUser(Long id,UserDTO user) {
        try{
            User userToUpdate =userRepository.findById(id).orElse(null);
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setEmail(user.getEmail());
            String password2 = bCryptPasswordEncoder.encode(user.getPassword());
            userToUpdate.setPassword(password2);
            userToUpdate.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(userToUpdate);
            return new UserDTO2(userToUpdate);
        }catch (Exception e){
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteUser(Long id) {
        try{
            User user = userRepository.findById(id).orElse(null);
            List<Account> accounts = accountService.getAllAccountsByUserId(id);

            if(user.getRoles().contains(Role.ROLE_ADMIN)){
                throw new IllegalArgumentException("Admin cannot be deleted");
            }else {
                for (Account account : accounts) {
                    if (account != null) {
                        accountService.deleteAccount(account.getIban());
                    }
                }
                userRepository.deleteById(id);
            }
        }
        catch (Exception e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    private List<UserDTO2> convertToUserDTO2(List<User> users) {
        List<UserDTO2> userDTO2s = new ArrayList<>();
        for (User user : users) {
            userDTO2s.add(new UserDTO2(user));
        }
        return userDTO2s;
    }

    public String login(LoginDTO loginDTO) throws Exception {
        User user = userRepository.findByEmail(loginDTO.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));

//        if (loginDTO.getPassword().equals(user.getPassword())) {
//            String token = jwtTokenProvider.createToken(loginDTO.getUsername(), user.getRoles());
//            return token;
//        } else {
//            throw new AuthenticationException("Incorrect username/password");
//        }

        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.createToken(loginDTO.getUsername(), user.getRoles());
            return token;
        } else {
            throw new AuthenticationException("Incorrect username/password");
        }

    }

    public List<UserDTO2> getAllUsersWithoutAccount() {
        List<User> users = (List<User>) userRepository.findAllByHasCurrentAccountIsFalseAndHasSavingsAccountIsFalse();
        List<UserDTO2>userDTO2s= convertToUserDTO2(users);
        return userDTO2s;
    }

    public User getFullUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user ==null){
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    public User getUserByEmail(String username) {
        User user = userRepository.findByEmail(username).orElse(null);
        if(user ==null){
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }
}
