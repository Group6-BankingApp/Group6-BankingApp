package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionService() {
    }

    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(new Transaction(transaction.getUserId(), transaction.getTimeCreated(), transaction.getSenderIban(), transaction.getRecieverIban(), transaction.getAmount()));
    }

    public Transaction Deposit(Transaction transaction) {
        return transactionRepository.save(new Transaction(transaction.getUserId(), transaction.getTimeCreated(), transaction.getSenderIban(), transaction.getRecieverIban(), transaction.getAmount()));
    }

    public Transaction Withdraw(Transaction transaction) {
        return transactionRepository.save(new Transaction(transaction.getUserId(), transaction.getTimeCreated(), transaction.getSenderIban(), transaction.getRecieverIban(), transaction.getAmount()));
    }
}
