package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {

        return userRepository.findById(id).orElse(null);
    }

    public User addUser(UserDTO userDTO) {
        return userRepository.save(new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getPhoneNumber(), Role.CUSTOMER));
    }

    public User updateUser(Long id,UserDTO user) {
        try{
            User userToUpdate=userRepository.findById(id).orElse(null);
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setPhoneNumber(user.getPhoneNumber());
            return  userRepository.save(userToUpdate);
        }catch (Exception e){
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
