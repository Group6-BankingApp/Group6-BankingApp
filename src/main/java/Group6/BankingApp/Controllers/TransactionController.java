package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private  final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try{
            return ResponseEntity.ok().body(transactionService.getAllTransactions());
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        try{
            return ResponseEntity.ok().body(transactionService.getTransactionById(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
        try{
            Transaction newTransaction=transactionService.addTransaction(transaction);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
    @PostMapping(value = "/deposit")
    public ResponseEntity Deposit(@RequestBody Transaction transaction) {
        try{
            Transaction newTransaction=transactionService.addTransactionDeposit(transaction);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
    @PostMapping(value = "/withdraw")
    public ResponseEntity Withdraw(@RequestBody Transaction transaction) {
        try{
            Transaction newTransaction=transactionService.addTransactionWithdraw(transaction);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
}