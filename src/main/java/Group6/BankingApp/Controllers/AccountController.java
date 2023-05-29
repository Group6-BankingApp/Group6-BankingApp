package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

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

//    @GetMapping
//    public ResponseEntity<List<AccountDTO>> getAccounts(
//            @RequestParam(value = "skip", required = false) Integer skip,
//            @RequestParam(value = "limit", required = false) Integer limit
//    ) {
//        try {
//            if (skip != null && skip < 0) {
//                return ResponseEntity.badRequest().build();
//            } else if (skip == null) {
//                skip = 0;
//            }
//            if (limit != null && (limit < 0 || limit > 50)) {
//                return ResponseEntity.badRequest().build();
//            } else if (limit == null) {
//                limit = 0;
//            }
//            List<AccountDTO> accounts = null;
//            try{
//                 accounts = accountService.getAccountsWithSkipAndLimit(skip, limit);
//            }catch(Exception ex)
//            {
//                //pass exception to external try catch
//                throw ex;
//            }
//            return ResponseEntity.ok().body(accounts);
//        } catch (Exception e) {
//            //return ResponseEntity.notFound().build();
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
//        }
//    }

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
    public ResponseEntity<Account> createAccount(@RequestBody AccountDTO newAccount) {
        try {
            if (newAccount == null) {
                return ResponseEntity.<AccountDTO>badRequest().build();
            }

            String generatedIban = generateIban();
            newAccount.setIban(generatedIban);
            Account createdAccount = accountService.addAccount(newAccount);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.<Account>badRequest().build();
        }
    }

    private String generateIban() { //Still to be detailed, static for now
        return "NL02ABNA0123456789";
    }

    @GetMapping("/{Iban}")
    public ResponseEntity<Account> getAccountByIban(@PathVariable("Iban") String Iban) {
        try {
            Account account = accountService.getAccountByIban(Iban);

            if (account != null) {
                return ResponseEntity.<Account>ok().body(account);
            } else {
                return ResponseEntity.<Account>notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.<Account>notFound().build();
        }
    }

    @PutMapping("/{Iban}")
    public ResponseEntity<Account> updateAccountByIban(
            @PathVariable("Iban") String Iban,
            @RequestBody AccountDTO updatedAccount
    ) {
        try {
            if (updatedAccount == null) {
                return ResponseEntity.badRequest().build();
            }
//            boolean isUpdated = accountService.updateAccountByIban(Iban, updatedAccount);
//
//            if (isUpdated) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.notFound().build();
//            }
            Account account = accountService.updateAccountByIban(Iban, updatedAccount);
            return ResponseEntity.ok().body(account);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{Iban}")
    public ResponseEntity<Void> deleteAccountByIban(@PathVariable("Iban") String Iban) {
        try {

//            boolean isDeleted = accountService.deleteAccountByIban(Iban);
//
//            if (isDeleted) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.notFound().build();
//            }
            accountService.deleteAccount(Iban);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/accounts/balance")
//    public ResponseEntity<Map<String, Double>> getAccountBalance(
//            @RequestParam("Iban") String Iban,
//            @RequestParam("pin") String pin
//    ) {
//        try {
//            double balance = accountService.getAccountBalance(Iban, pin);
//
//            if (balance >= 0) {
//                Map<String, Double> responseBody = new HashMap<>();
//                responseBody.put("balance", balance);
//                return ResponseEntity.ok().body(responseBody);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/balance")
    public double getAccountBalance(@RequestParam("iban") String iban, @RequestParam("pin") String pin) {
        try {
            return accountService.getAccountBalance(iban, pin);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve account balance", e);
        }
    }

    @PutMapping("/{Iban}/DebitCard")
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

    @PutMapping("/{Iban}/pin")
    public ResponseEntity<Account> updatePIN(
            @PathVariable("Iban") String Iban,
            @RequestBody AccountDTO accountDTO
    ) {
        try {
            if (accountDTO == null) {
                return ResponseEntity.badRequest().build();
            }

//            boolean isUpdated = accountService.updateAccount(accountDTO);
//
//            if (isUpdated) {
//                return ResponseEntity.status(HttpStatus.CREATED).build();
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
            Account account = accountService.updateAccountByIban(Iban, accountDTO);
            return ResponseEntity.ok().body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

