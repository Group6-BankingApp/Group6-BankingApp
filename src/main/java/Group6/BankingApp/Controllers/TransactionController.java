package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.AtmResponseDTO;
import Group6.BankingApp.Models.dto.AtmTransactionDTO;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Models.dto.FilterDTO;
import Group6.BankingApp.Services.TransactionService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {
    // private static final String SECRET_KEY = "fDKiV3Rq7t";
    private  final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

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

    @GetMapping(value = "/customer/{iban}")
    public ResponseEntity<List<Transaction>> getTransactionsByIban(@PathVariable String iban) {
        try{
            List<Transaction> transactions=transactionService.findAllTransactions(iban);
            return ResponseEntity.ok(transactions);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
        }
    }

    @PostMapping(value = "/customer/{iban}/filter")
    public ResponseEntity<List<Transaction>> getFilteredTransactionsByIban(@PathVariable String iban,@RequestBody FilterDTO filter) {
        try{
            List<Transaction> transactions = transactionService.findTransactionsByFilter(iban, filter);
            return ResponseEntity.ok(transactions);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
        }
    }

    private boolean allFilterEmpty(FilterDTO filter) {
        return filter.getStartDate()==null && filter.getEndDate()==null && filter.getMinAmount()==null && filter.getMaxAmount()==null && filter.getAccount()==null && filter.getFromOrTo()==null;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        try{
            return ResponseEntity.ok().body(transactionService.getTransactionById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public ResponseEntity addTransaction(@RequestBody Transaction transaction, @RequestParam(defaultValue = "0000") String pin, @RequestHeader("Authorization") String token) {
//        try{
//            Transaction newTransaction=transactionService.addTransaction(transaction, pin);
//            return ResponseEntity.status(201).body(newTransaction);
//        }catch (Exception e){
//            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
//        }
//    }
    @PostMapping(value = "/deposit")
    public ResponseEntity<AtmResponseDTO> Deposit(@RequestBody AtmTransactionDTO transaction){
        try{
            AtmResponseDTO responseDTO = transactionService.makeDeposit(transaction);
            return ResponseEntity.status(201).body(responseDTO);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deposit", e);
        }
    }
    
    @PostMapping(value = "/withdraw")
    public ResponseEntity<AtmResponseDTO> Withdraw(@RequestBody AtmTransactionDTO transaction) {
        try{
            AtmResponseDTO responseDTO = transactionService.makeWithdraw(transaction);
            return ResponseEntity.status(201).body(responseDTO);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to withdraw", e);
        }
    }

    @PostMapping
    public ResponseEntity makeTransfer (@RequestBody TransactionDTO transactionDTO) {
        try{
            Transaction newTransaction=transactionService.addTransaction(transactionDTO);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
}
