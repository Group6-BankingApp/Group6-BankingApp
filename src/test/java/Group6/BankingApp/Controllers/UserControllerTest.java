package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO2 userDTO2;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@gmail.com");
        user1.setPassword("123456");
        user1.setPhoneNumber("0612345678");
        user1.setHasAccount(true);
        user1.setRoles(List.of(Role.ROLE_USER));
        userDTO2 = new UserDTO2(user1);
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getAllUsersWithAccountTest() throws Exception{
        when(userService.getAllUsersWithAccount()).thenReturn(List.of(userDTO2));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/withAccount")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getUserByIdTest() throws Exception{
        when(userService.getUserById(any(Long.class))).thenReturn(userDTO2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void addUserTest() throws Exception{
        when(userService.addUser(any(UserDTO.class))).thenReturn(userDTO2);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updateUserTest() throws Exception{
        when(userService.updateUser(any(Long.class),any(UserDTO.class))).thenReturn(userDTO2);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void deleteUserTest() throws Exception{
        doNothing().when(userService).deleteUser(any(Long.class));
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
    }
}