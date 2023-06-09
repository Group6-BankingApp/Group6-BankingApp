package Group6.BankingApp.Controllers;

import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Services.TransactionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.hibernate.service.spi.ServiceException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
    // private static final String SECRET_KEY = "fDKiV3Rq7t";
    private  final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            //@RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "NL67INGB1234567890") String iban,
            @RequestParam(defaultValue = "0") Integer skip,
            @RequestParam(defaultValue = "40") Integer limit,
            @RequestParam(defaultValue = "") String dateFrom,
            @RequestParam(defaultValue = "") String dateTo,
            @RequestParam(defaultValue = "") String pin
    ) {
        try {
            // Remove "Bearer " prefix from the token string
            // String jwtToken = token.replace("Bearer ", "");

            // // // Validate and parse the JWT token
            // Claims claims = Jwts.parserBuilder()
            //     .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            //     .build()
            //     .parseClaimsJws(jwtToken)
            //     .getBody();

            // // // Extract user information from the claims
            // String userId = claims.getSubject();
            // // Perform additional authentication and authorization checks if needed

            if (dateTo.isEmpty()) {
                LocalDate oneYearAgo = LocalDate.now().minusYears(1);
                dateFrom = oneYearAgo.toString();
            }

            if (dateTo.isEmpty()) {
                LocalDate today = LocalDate.now();
                dateTo = today.toString();
            }

            if (pin.isEmpty()) {
                // pin = "invalid";
            }

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
            List<Transaction> transactions = null;
            try {
                transactions = transactionService.findAllTransactions(skip, limit, dateFrom, dateTo, iban, pin);
                return ResponseEntity.ok(transactions);
            }catch (Exception e){
                throw e;
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve transactions", e);
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
    public ResponseEntity addTransaction(@RequestBody Transaction transaction, @RequestParam(defaultValue = "0000") String pin, @RequestHeader("Authorization") String token) {
        try{
            Transaction newTransaction=transactionService.addTransaction(transaction, pin);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
    @PostMapping(value = "/deposit")
    public ResponseEntity Deposit(@RequestBody Transaction transaction, @RequestHeader("Authorization") String token) {
        try{
            Transaction newTransaction=transactionService.addTransactionDeposit(transaction);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
    
    @PostMapping(value = "/withdraw")
    public ResponseEntity Withdraw(@RequestBody Transaction transaction, @RequestParam(defaultValue = "0000") String pin, @RequestHeader("Authorization") String token) {
        try{
            Transaction newTransaction=transactionService.addTransactionWithdraw(transaction, pin);
            return ResponseEntity.status(201).body(newTransaction);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getCause().getMessage());
        }
    }
}
