package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AtmLoginDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO2;
import Group6.BankingApp.Services.DebitCardService;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://group6-rbank.netlify.app")
@RequestMapping(value="/debitcards", produces = MediaType.APPLICATION_JSON_VALUE)
@Log
public class DebitCardController {

    private final DebitCardService debitCardService;

    public DebitCardController(DebitCardService debitCardService) {
        this.debitCardService = debitCardService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @GetMapping(value = "/{iban}")
    public ResponseEntity<List<DebitCardDTO2>> getDebitCardsByIban(@PathVariable String iban) {
        try {
            return ResponseEntity.ok(debitCardService.getDebitCardsByIban(iban));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
