package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Customer;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.util.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Customer> customers = (List<Customer>) userRepository.findAll();
        List<UserDTO2>userDTO2s= convertToUserDTO2(customers);
        return userDTO2s;
    }

    public List<UserDTO2> getAllUsersWithAccount() {
        List<Customer> customers = (List<Customer>) userRepository.findAllByHasAccountIsTrue();
        List<UserDTO2>userDTO2s= convertToUserDTO2(customers);
        return userDTO2s;
    }

    public UserDTO2 getUserById(Long id) {

        Customer customer = userRepository.findById(id).orElse(null);
        if(customer ==null){
            throw new EntityNotFoundException("User not found");
        }
        UserDTO2 userdto = new UserDTO2(customer);
        return userdto;
    }

    public UserDTO2 addUser(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isEmpty()){
            //String password = bCryptPasswordEncoder.encode(userDTO.getPassword());
            Customer customer = new Customer();
            customer.setFirstName(userDTO.getFirstName());
            customer.setLastName(userDTO.getLastName());
            customer.setEmail(userDTO.getEmail());
            customer.setPassword(userDTO.getPassword());
            customer.setPhoneNumber(userDTO.getPhoneNumber());
            customer.setRoles(List.of(Role.ROLE_USER));
            customer.setHasAccount(false);
            UserDTO2 userdto = new UserDTO2(userRepository.save(customer));
            return userdto;
        }
       throw new IllegalArgumentException("Email is already taken");
    }

    public UserDTO2 updateUser(Long id,UserDTO user) {
        try{
            Customer customerToUpdate =userRepository.findById(id).orElse(null);
            customerToUpdate.setFirstName(user.getFirstName());
            customerToUpdate.setLastName(user.getLastName());
            customerToUpdate.setEmail(user.getEmail());
            customerToUpdate.setPassword(user.getPassword());
            customerToUpdate.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(customerToUpdate);
            UserDTO2 userdto = new UserDTO2(customerToUpdate);
            return userdto;
        }catch (Exception e){
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private List<UserDTO2> convertToUserDTO2(List<Customer> customers) {
        List<UserDTO2> userDTO2s = new ArrayList<>();
        for (Customer customer : customers) {
            userDTO2s.add(new UserDTO2(customer));
        }
        return userDTO2s;
    }

    public String login(LoginDTO loginDTO) throws Exception {
        Customer customer = userRepository.findByEmail(loginDTO.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (loginDTO.getPassword().equals(customer.getPassword())) {
            String token = jwtTokenProvider.createToken(loginDTO.getUsername(), customer.getRoles());
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
        List<Customer> customers = (List<Customer>) userRepository.findAllByHasAccountIsFalse();
        List<UserDTO2>userDTO2s= convertToUserDTO2(customers);
        return userDTO2s;
    }
}
