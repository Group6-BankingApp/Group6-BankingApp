package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.DAL.UserRepository;
import Group6.BankingApp.Models.*;
import Group6.BankingApp.Models.dto.LoginDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.util.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collections;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    @Autowired
    private UserService userService;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void testAddUser_UniqueEmail() {
        // Arrange
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gamil.com", "123456", "1234567890");
        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        // Act
        UserDTO2 result = userService.addUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(newUser.getId(), result.getId());
        assertEquals(newUser.getFirstName(), result.getFirstName());
        assertEquals(newUser.getLastName(), result.getLastName());
        assertEquals(newUser.getEmail(), result.getEmail());
        assertEquals(newUser.getPhoneNumber(), result.getPhoneNumber());
        assertEquals("No", result.getHasCurrentAccount());
    }

    @Test
    void testAddUser_UsedEmail() {
        // Arrange
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gmail.com", "123456", "1234567890");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void testUpdateUser_UserExists() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("John", "Doe", "johndoe@example.com", "123456", "1234567890");

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
        assertEquals("No", result.getHasCurrentAccount());
    }

    @Test
    void testUpdateUser_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = new UserDTO("Mohamad", "Mokhtar", "mohamad@gmail.com", "123456", "1234567890");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, userDTO));
    }

    @Test
    void testLogin_UserExists_CorrectCredentials() throws Exception {
        // Arrange
        String username = "mohamad@gmail.com";
        String password = "123456";

        LoginDTO loginDTO = new LoginDTO(username, password);

        User existingUser = new User();
        existingUser.setEmail(username);
        existingUser.setPassword(password);
        existingUser.setRoles(List.of(Role.ROLE_USER));

        BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        when(bCryptPasswordEncoder.matches(eq(password), anyString())).thenReturn(true);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(existingUser));
        when(jwtTokenProvider.createToken(username, existingUser.getRoles())).thenReturn("token");

        AccountService accountService = mock(AccountService.class);

        UserService userService = new UserService(userRepository, bCryptPasswordEncoder, jwtTokenProvider, accountService);

        // Act
        String result = userService.login(loginDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testLogin_UserExists_IncorrectCredentials() {
        // Arrange
        String username = "mohamad@gmail.com";
        String password = "223344";

        LoginDTO loginDTO = new LoginDTO(username, password);

        User existingUser = new User();
        existingUser.setEmail(username);
        existingUser.setPassword("123456");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> userService.login(loginDTO));
    }

    @Test
    void testLogin_UserDoesNotExist() {
        // Arrange
        String username = "nonexistent@example.com";
        String password = "password";

        LoginDTO loginDTO = new LoginDTO(username, password);

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> userService.login(loginDTO));
    }

    @Test
    void testGetAllUsersWithoutAccount_ReturnsUsersList() {
        // Arrange
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Moahad");
        user1.setLastName("Mokhtar");
        user1.setEmail("mohamad@gmail.com");
        user1.setPhoneNumber("0655987321");
        user1.setHasCurrentAccount(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Abdelilah");
        user2.setLastName("Ziani");
        user2.setEmail("abdelilah@email.com");
        user2.setPhoneNumber("0678912345");
        user2.setHasCurrentAccount(false);

        users.add(user1);
        users.add(user2);

        when(userRepository.findAllByHasCurrentAccountIsFalseAndHasSavingsAccountIsFalse()).thenReturn(users);

        // Act
        List<UserDTO2> result = userService.getAllUsersWithoutAccount();

        // Assert
        assertNotNull(result);
        assertEquals(users.size(), result.size());

        UserDTO2 userDTO2_1 = result.get(0);
        assertEquals(user1.getId(), userDTO2_1.getId());
        assertEquals(user1.getFirstName(), userDTO2_1.getFirstName());
        assertEquals(user1.getLastName(), userDTO2_1.getLastName());
        assertEquals(user1.getEmail(), userDTO2_1.getEmail());
        assertEquals(user1.getPhoneNumber(), userDTO2_1.getPhoneNumber());
        assertEquals("No", userDTO2_1.getHasCurrentAccount());

        UserDTO2 userDTO2_2 = result.get(1);
        assertEquals(user2.getId(), userDTO2_2.getId());
        assertEquals(user2.getFirstName(), userDTO2_2.getFirstName());
        assertEquals(user2.getLastName(), userDTO2_2.getLastName());
        assertEquals(user2.getEmail(), userDTO2_2.getEmail());
        assertEquals(user2.getPhoneNumber(), userDTO2_2.getPhoneNumber());
        assertEquals("No", userDTO2_2.getHasCurrentAccount());
    }
}
