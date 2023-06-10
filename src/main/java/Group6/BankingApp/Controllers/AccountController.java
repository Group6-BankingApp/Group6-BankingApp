package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Models.dto.NewAccountDTO;
import Group6.BankingApp.Services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping("/{iban}/debit-cards")
    public ResponseEntity<DebitCardDTO> createDebitCard(@PathVariable String iban) {
        Account account = accountService.findAccountByIban(iban);
        if (account == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createDebitCard(account));
    }

    @PostMapping("/{iban}/debit-cards/activate")
    public ResponseEntity<DebitCardDTO> activateDebitCard(@RequestParam("cardUUID") String cardUUID, @RequestParam("pin") String pin) {
        DebitCardDTO activatedCard = accountService.activateDebitCard(cardUUID, pin);
        if (activatedCard == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(activatedCard);
        }
    }

//    @PutMapping("/{iban}/debit-cards/deactivate")
//    public ResponseEntity<String> deactivateDebitCard(@PathVariable("iban") String iban, @RequestBody DebitCardDTO debitCardDTO) {
//        try {
//            accountService.deactivateDebitCard(iban, debitCardDTO);
//            return ResponseEntity.ok("Debit card deactivated successfully.");
//        } catch (Exception ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to deactivate debit card!");
//        }
//    }

    @PutMapping("/{iban}/DebitCard")
    public ResponseEntity<Void> deactivateDebitCard(
            @PathVariable("Iban") String Iban,
            @RequestBody DebitCardDTO debitCardDTO
    ) {
        try {
            if (debitCardDTO == null) {
                return ResponseEntity.notFound().build();
            }

//          boolean isDeactivated = accountService.deactivateAccount(Iban, debitCardDTO);
            else {
                accountService.deactivateDebitCard(Iban, debitCardDTO);
                return ResponseEntity.ok().build();
            }
//            if (isDeactivated) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.notFound().build();
//            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deactivate account", e);
        }
    }

    @PutMapping("/{iban}/pin")
    public ResponseEntity<NewAccountDTO> updatePin(@PathVariable("iban") String iban,
                                                   @RequestBody AccountDTO accountDTO) {
        NewAccountDTO updatedAccountDTO = accountService.updatePin(iban, accountDTO);
        return ResponseEntity.ok().body(updatedAccountDTO);
    }

}

