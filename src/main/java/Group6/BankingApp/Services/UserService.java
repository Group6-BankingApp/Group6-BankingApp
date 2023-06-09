package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.Login2DTO;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public UserService() {
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

        User user= userRepository.findById(id).orElse(null);
        if(user==null){
            throw new EntityNotFoundException("User not found");
        }
        return new UserDTO2(user);
    }

    public User getFullUserById(Long id) {

        User user= userRepository.findById(id).orElse(null);
        if(user==null){
            throw new EntityNotFoundException("User not found");
        }

        return user;
    }

    public UserDTO2 addUser(UserDTO userDTO) {
        User user= userRepository.save(new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getPhoneNumber(), Role.CUSTOMER, false));
        UserDTO2 userdto = new UserDTO2(user);
        return userdto;
    }

    public UserDTO2 updateUser(Long id,UserDTO user) {
        try{
            User userToUpdate=userRepository.findById(id).orElse(null);
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

    public Login2DTO login(LoginDTO loginDTO) throws AuthenticationException {
        User user = userRepository.findByEmail(loginDTO.getUsername());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new AuthenticationException("Wrong password");
        }
        Login2DTO login2DTO = new Login2DTO(user);
        return login2DTO;
    }
}
