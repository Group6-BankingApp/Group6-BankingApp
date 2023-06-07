package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.dto.*;
import Group6.BankingApp.Services.UserService;
import Group6.BankingApp.util.JwtUtil;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173")
@Log
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Login2DTO> login(@RequestBody LoginDTO loginDTO) throws AuthenticationException {
        return ResponseEntity.ok(userService.login(loginDTO));
    }

    @GetMapping(value = "/withAccount")
    public ResponseEntity<List<UserDTO2>> getAllUsersWithAccount() {
        return ResponseEntity.ok(userService.getAllUsersWithAccount());
    }

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
