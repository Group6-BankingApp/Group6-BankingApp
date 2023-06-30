package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.dto.*;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.DebitCardService;
import lombok.extern.java.Log;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://group6-rbank.netlify.app")
@RequestMapping(value="/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Log
public class AccountController {

    private final AccountService accountService;
    @Autowired
    private  DebitCardService debitCardService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/savings/{id}")
    public ResponseEntity<SavingsAccountDTO> createSavingsAccount(@PathVariable Long id) {
        try {
            SavingsAccountDTO savingsAccountDTO = accountService.addSavingsAccount(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(savingsAccountDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{iban}")
    public ResponseEntity<NewAccountDTO> updateAccountByIban(@PathVariable("iban") String iban,
                                                             @RequestBody AccountDTO accountDTO) {
        Account account = accountService.findAccountByIban(iban);
        NewAccountDTO updatedAccountDTO = accountService.updateAccountByIban(iban, account);
        return ResponseEntity.ok().body(updatedAccountDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{iban}/delete")
    public ResponseEntity<Void> deleteAccountByIban(@PathVariable String iban) {
        try {
            accountService.deleteAccount(iban);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/balance")
    public double getAccountBalance(@RequestParam("iban") String iban, @RequestParam("pin") String pin) {
        try {
            return accountService.getAccountBalance(iban, pin);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve account balance", e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{iban}/debitcard")
    public ResponseEntity<DebitCardDTO> createDebitCard(@PathVariable String iban) {
        Account account = accountService.findAccountByIban(iban);
        if (account == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(debitCardService.createDebitCard(account));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{iban}/debitcard/activate")
    public ResponseEntity<DebitCardDTO> activateDebitCard(@RequestParam("cardUUID") String cardUUID, @RequestParam("pin") String pin) {
        DebitCardDTO activatedCard = accountService.activateDebitCard(cardUUID, pin);
        if (activatedCard == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(activatedCard);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{iban}/deactivateDebitCard/{cardNumber}")
    public ResponseEntity<String> deactivateDebitCard(@PathVariable("iban") String iban, @PathVariable String cardNumber) {
        if (cardNumber == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            accountService.deactivateDebitCard(iban, cardNumber, false);
            return ResponseEntity.ok("Debit card deactivated successfully.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<AccountDTO> updatePin(@RequestBody AccountDTO accountDTO) {
        try{
            AccountDTO updatedAccountDTO = accountService.updatePin(accountDTO);
            return ResponseEntity.ok().body(updatedAccountDTO);
        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update pin", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/customer/{id}")
    public List<AccountDTO> getAllAccountsByCustomerId(@PathVariable("id") Long id) {
        try {
            return  accountService.getAllAccountsByCustomerId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/customer/Current")
    public List<AccountDTO> getCurrentAccountsByCustomerId(@RequestBody Map<String, Long> requestBody) {
        try {
            return  accountService.getCurrentAccountsByCustomerId(requestBody.get("id"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/customer/current/{id}")
    public List<AccountDTO> getAccountsByCustomerId(@PathVariable("id") Long id) {
        try {
            return  accountService.getCurrentAccountsByCustomerId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/customer/Savings/{id}")
    public  List<SavingsAccountDTO> getSavingsAccountsByCustomerId(@PathVariable("id") Long id) {
        try {
            return  accountService.getSavingsAccountsByCustomerId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve accounts", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/customerIban")
    public String getIbanbyFullName(@RequestBody FullNameDTO fullNameDTO) {
        try {
            return  accountService.getIbanbyFullName(fullNameDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

