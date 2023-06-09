package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.UserRepository;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<UserDTO2> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        List<UserDTO2>userDTO2s= convertToUserDTO2(users);
        return userDTO2s;
    }

    public List<UserDTO2> getAllUsersWithAccount() {
        List<User> users = (List<User>) userRepository.findAllByHasAccountIsTrue();
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
            //String password = bCryptPasswordEncoder.encode(userDTO.getPassword());
            User user = new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setRoles(List.of(Role.ROLE_USER));
            user.setHasAccount(false);
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
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(userToUpdate);
            UserDTO2 userdto = new UserDTO2(userToUpdate);
            return userdto;
        }catch (Exception e){
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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

        if (loginDTO.getPassword().equals(user.getPassword())) {
            String token = jwtTokenProvider.createToken(loginDTO.getUsername(), user.getRoles());
            return token;
        } else {
            throw new AuthenticationException("Incorrect username/password");
        }

//        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), customer.getPassword())) {
//            String token = jwtTokenProvider.createToken(loginDTO.getUsername(), customer.getRoles());
//            Login2DTO login2DTO = new Login2DTO(customer.getRoles(),token);
//            return login2DTO;
//        } else {
//            throw new AuthenticationException("Incorrect username/password");
//        }

    }

    public List<UserDTO2> getAllUsersWithoutAccount() {
        List<User> users = (List<User>) userRepository.findAllByHasAccountIsFalse();
        List<UserDTO2>userDTO2s= convertToUserDTO2(users);
        return userDTO2s;
    }
}
