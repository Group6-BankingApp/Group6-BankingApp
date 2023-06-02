package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.dto.ExceptionDTO;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Models.dto.UserDTO2;
import Group6.BankingApp.Services.UserService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Log
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
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

    private ResponseEntity<Object> handleException(int status, Exception ex) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(status, ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.status(status).body(exceptionDTO);
    }
}
