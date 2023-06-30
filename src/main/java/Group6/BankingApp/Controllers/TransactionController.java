package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.AtmResponseDTO;
import Group6.BankingApp.Models.dto.AtmTransactionDTO;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Models.dto.FilterDTO;
import Group6.BankingApp.Services.TransactionService;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://group6-banking-app-front-end.vercel.app")
public class TransactionController {
    // private static final String SECRET_KEY = "fDKiV3Rq7t";
    private  final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.findAll();
            return ResponseEntity.ok(transactions);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/customer/{iban}")
    public ResponseEntity<List<Transaction>> getTransactionsByIban(@PathVariable String iban) {
        try{
            List<Transaction> transactions=transactionService.findAllTransactions(iban);
            return ResponseEntity.ok(transactions);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping(value = "/customer/{iban}/filter")
    public ResponseEntity<List<Transaction>> getFilteredTransactionsByIban(@PathVariable String iban,@RequestBody FilterDTO filter) {
        try{
            List<Transaction> transactions = transactionService.findTransactionsByFilter(iban, filter);
            return ResponseEntity.ok(transactions);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        try{
            return ResponseEntity.ok().body(transactionService.getTransactionById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(value = "/deposit")
    public ResponseEntity<AtmResponseDTO> Deposit(@RequestBody AtmTransactionDTO transaction){
        try{
            AtmResponseDTO responseDTO = transactionService.makeDeposit(transaction);
            return ResponseEntity.status(201).body(responseDTO);
        }catch(ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  e.getMessage());
        }
    }
    
    @PostMapping(value = "/withdraw")
    public ResponseEntity<AtmResponseDTO> Withdraw(@RequestBody AtmTransactionDTO transaction) {
        try{
            AtmResponseDTO responseDTO = transactionService.makeWithdraw(transaction);
            return ResponseEntity.status(201).body(responseDTO);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,  e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity makeTransfer (@RequestBody TransactionDTO transactionDTO) {
        try{
            Transaction newTransaction=transactionService.addTransfer(transactionDTO);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
}
