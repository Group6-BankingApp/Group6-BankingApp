package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService = new AccountService();


    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(
            @RequestParam(value = "skip", required = false) Integer skip,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        try {
            if (skip != null && skip < 0) {
                return ResponseEntity.badRequest().build();
            }
            if (limit != null && (limit < 0 || limit > 50)) {
                return ResponseEntity.badRequest().build();
            }

            List<AccountDTO> accounts = accountService.getAccounts(skip, limit);

            return ResponseEntity.ok().body(accounts);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody NewAccountDTO newAccount) {
        try {
            if (newAccount == null) {
                return ResponseEntity.<AccountDTO>badRequest().build();
            }

            String generatedIban = generateIban();
            Account x = new Account(newAccount);
            x.setIban(generatedIban);
            AccountDTO createdAccount = accountService.addAccount(x);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.<AccountDTO>badRequest().build();
        }
    }

    private String generateIban() { //Still to be detailed, static for now
        return "NL02ABNA0123456789";
    }

    @GetMapping("/accounts/{Iban}")
    public ResponseEntity<AccountDTO> getAccountByIban(@PathVariable("Iban") String Iban) {
        try {
            AccountDTO account = accountService.getAccountByIban(Iban);

            if (account != null) {
                return ResponseEntity.<AccountDTO>ok().body(account);
            } else {
                return ResponseEntity.<AccountDTO>notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.<AccountDTO>notFound().build();
        }
    }

    @PutMapping("/accounts/{Iban}")
    public ResponseEntity<Void> updateAccountByIban(
            @PathVariable("Iban") String Iban,
            @RequestBody AccountDTO updatedAccount
    ) {
        try {
            if (updatedAccount == null) {
                return ResponseEntity.badRequest().build();
            }

            boolean isUpdated = accountService.updateAccountByIban(Iban, updatedAccount);

            if (isUpdated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/accounts/{Iban}")
    public ResponseEntity<Void> deleteAccountByIban(@PathVariable("Iban") String Iban) {
        try {

            boolean isDeleted = accountService.deleteAccountByIban(Iban);

            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/accounts/balance")
    public ResponseEntity<Map<String, Double>> getAccountBalance(
            @RequestParam("Iban") String Iban,
            @RequestParam("pin") String pin
    ) {
        try {
            double balance = accountService.getAccountBalance(Iban, pin);

            if (balance >= 0) {
                Map<String, Double> responseBody = new HashMap<>();
                responseBody.put("balance", balance);
                return ResponseEntity.ok().body(responseBody);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/accounts/{Iban}/DebitCard")
    public ResponseEntity<Void> deactivateDebitCard(
            @PathVariable("Iban") String Iban,
            @RequestBody DebitCardDTO debitCardDTO
    ) {
        try {
            if (debitCardDTO == null) {
                return ResponseEntity.badRequest().build();
            }

            boolean isDeactivated = accountService.deactivateDebitCard(Iban, debitCardDTO);

            if (isDeactivated) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/accounts/{Iban}/pin")
    public ResponseEntity<Void> updatePIN(
            @PathVariable("Iban") String Iban,
            @RequestBody AccountDTO accountDTO
    ) {
        try {
            if (accountDTO == null) {
                return ResponseEntity.badRequest().build();
            }

            boolean isUpdated = accountService.updateAccount(accountDTO);

            if (isUpdated) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

