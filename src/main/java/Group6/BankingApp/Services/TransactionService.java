package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
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

    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction addTransaction(Transaction transaction) {
        try {
            Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    transaction.getRecieverIban(),
                    transaction.getAmount(),
                    transaction.getDescription()
            );
            return transactionRepository.save(newtransaction);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }

    public Transaction Deposit(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction Withdraw(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
