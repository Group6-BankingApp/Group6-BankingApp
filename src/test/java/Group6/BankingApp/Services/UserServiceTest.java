package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.*;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddUser_UniqueEmail() {
        // Arrange
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gamil.com", "123456", "1234567890", "No");
        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        UserDTO2 result = userService.addUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(newUser.getId(), result.getId());
        assertEquals(newUser.getFirstName(), result.getFirstName());
        assertEquals(newUser.getLastName(), result.getLastName());
        assertEquals(newUser.getEmail(), result.getEmail());
        assertEquals(newUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals("No", result.getHasAccount());
    }

    @Test
    void testAddUser_UsedEmail() {
        // Arrange
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gmail.com", "123456", "1234567890", "No");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("John", "Doe", "johndoe@example.com", "123456", "1234567890", "No");

        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        UserDTO2 result = userService.updateUser(userId, userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(userDTO.getFirstName(), result.getFirstName());
        assertEquals(userDTO.getLastName(), result.getLastName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPhoneNumber(), result.getPhoneNumber());
        assertEquals("No", result.getHasAccount());
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gmail.com", "123456", "1234567890", "No");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, userDTO));
    }
}
