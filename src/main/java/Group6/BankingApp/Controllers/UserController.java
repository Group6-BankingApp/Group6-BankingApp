package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.Role;
import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.*;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.UserService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173")
@Log
public class UserController {

    private  final UserService userService;
    private final AccountService accountService;


    public UserController(UserService userService, AccountService accountService) {

        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping(value = "/login")
    public Object login(@RequestBody LoginDTO loginDTO) throws Exception {
        User user = userService.getUserByEmail(loginDTO.getUsername());
        return new TokenDTO(user.getId(),user.getFirstName(),userService.login(loginDTO));
    }

    @GetMapping(value = "/withAccount")
    public ResponseEntity<List<UserDTO2>> getAllUsersWithAccount() {
        return ResponseEntity.ok(userService.getAllUsersWithAccount());
    }

    @GetMapping(value = "/withoutAccount")
    public ResponseEntity<List<UserDTO2>> getAllUsersWithoutAccount() {
        return ResponseEntity.ok(userService.getAllUsersWithoutAccount());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO2>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO2> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO2> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO2> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO2 user=userService.updateUser(id, userDTO);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
