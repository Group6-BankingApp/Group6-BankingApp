package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.User;
import Group6.BankingApp.Models.dto.UserDTO;
import Group6.BankingApp.Services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            return ResponseEntity.ok().body(userService.getAllUsers());
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok().body(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody UserDTO userDTO) {
        try{
            User newUser=userService.addUser(userDTO);
            return ResponseEntity.status(201).body(newUser);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try{
            User user=userService.updateUser(id, userDTO);
            return ResponseEntity.ok().body(user);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
