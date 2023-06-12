package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Services.AccountService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Log
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(
            @RequestParam(defaultValue = "0") Integer skip,
            @RequestParam(defaultValue = "40") Integer limit
    ) {
        try {
            if (skip != null && skip < 0) {
                return ResponseEntity.badRequest().build();
            } else if (skip == null) {
                skip = 0;
            }
            if (limit != null && (limit < 0 || limit > 50)) {
                return ResponseEntity.badRequest().build();
            } else if (limit == null) {
                limit = 0;
            }
            List<AccountDTO> accounts = null;
            try {
                accounts = accountService.findAllAccounts(skip, limit);
                return ResponseEntity.ok(accounts);
            }catch (Exception e){
                throw e;
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody NewAccountDTO newAccountDTO) {
        if (newAccountDTO == null) {
            // Return a BAD_REQUEST response if the newAccountDTO is empty
            return ResponseEntity.badRequest().build();
        }

        try {
            AccountDTO accountDTO = accountService.addAccount(newAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountDTO());
        }
    }

    @GetMapping("/{iban}")
    public ResponseEntity<AccountDTO> getAccountByIban(@PathVariable("iban") String iban) {
        try {
            AccountDTO account = accountService.getAccountByIban(iban);

            if (account != null) {
                return ResponseEntity.<Account>ok().body(account);
            } else {
                return ResponseEntity.<Account>notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{iban}")
    public ResponseEntity<NewAccountDTO> updateAccountByIban(@PathVariable("iban") String iban,
                                                             @RequestBody AccountDTO accountDTO) {
        NewAccountDTO updatedAccountDTO = accountService.updateAccountByIban(iban, accountDTO);
        return ResponseEntity.ok().body(updatedAccountDTO);
    }

    @DeleteMapping("/{iban}")
    public ResponseEntity<Void> deleteAccountByIban(@PathVariable("Iban") String Iban) {
        try {
            accountService.deleteAccount(Iban);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/balance")
    public double getAccountBalance(@RequestParam("iban") String iban, @RequestParam("pin") String pin) {
        try {
            return accountService.getAccountBalance(iban, pin);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve account balance", e);
        }
    }

    @PostMapping("/{iban}/debitcard")
    public ResponseEntity<DebitCardDTO> createDebitCard(@PathVariable String iban) {
        Account account = accountService.findAccountByIban(iban);
        if (account == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createDebitCard(account));
    }

    @PostMapping("/{iban}/debitcard/activate")
    public ResponseEntity<DebitCardDTO> activateDebitCard(@RequestParam("cardUUID") String cardUUID, @RequestParam("pin") String pin) {
        DebitCardDTO activatedCard = accountService.activateDebitCard(cardUUID, pin);
        if (activatedCard == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(activatedCard);
        }
    }

    @PutMapping("/{iban}/debitcard/{cardNumber}/deactivate")
    public ResponseEntity<String> deactivateDebitCard(
            @PathVariable("iban") String iban,
            @PathVariable String cardNumber,
            @RequestParam(value = "active") Boolean active
    ) {
            accountService.deactivateDebitCard(iban, cardNumber, active);
            return ResponseEntity.ok("Debit card deactivated successfully.");
    }

    @PutMapping("/{iban}/pin")
    public ResponseEntity<NewAccountDTO> updatePin(@PathVariable("iban") String iban,
                                                   @RequestBody AccountDTO accountDTO) {
        NewAccountDTO updatedAccountDTO = accountService.updatePin(iban, accountDTO);
        return ResponseEntity.ok().body(updatedAccountDTO);
    }
    @GetMapping("/customer")
    public List<AccountDTO> getAccountsByCustomerId(@RequestBody Map<String, Long> requestBody) {
        try {
            return  accountService.getAccountsByCustomerId(requestBody.get("id"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }
    @GetMapping("/customer/{id}")
    public List<AccountDTO> getAccountsByCustomerId(@PathVariable("id") Long id) {
        try {
            return  accountService.getAccountsByCustomerId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }
}

