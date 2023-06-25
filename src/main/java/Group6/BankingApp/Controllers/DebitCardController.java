package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.dto.AtmLoginDTO;
import Group6.BankingApp.Services.DebitCardService;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value="/debitcards", produces = MediaType.APPLICATION_JSON_VALUE)
@Log
public class DebitCardController {

    private final DebitCardService debitCardService;

    public DebitCardController(DebitCardService debitCardService) {
        this.debitCardService = debitCardService;
    }

    @GetMapping
    public Object getAllDebitCards() {
        return debitCardService.getAllDebitCards();
    }

    @PostMapping(value = "/cardInsert")
    public ResponseEntity<Object> insertDebitCard(@RequestBody AtmLoginDTO atmLoginDTO) {
        try {
            return ResponseEntity.ok(debitCardService.insertDebitCard(atmLoginDTO));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
}
