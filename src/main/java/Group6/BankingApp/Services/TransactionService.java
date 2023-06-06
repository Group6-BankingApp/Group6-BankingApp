package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import java.util.List;
@Service
public class TransactionService {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
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
            if(CheckSufficientFunds(transaction)){
                TransferMoney(transaction);
                Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    transaction.getRecieverIban(),
                    transaction.getAmount(),
                    "regular transaction"
                    );
                    return transactionRepository.save(newtransaction);
                }
                else {
                    throw new ServiceException("Insufficient funds");
                }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionDeposit(Transaction transaction) {
        try {
            if(CheckSufficientFunds(transaction)){
            Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    transaction.getRecieverIban(),
                    transaction.getAmount(),
                    "deposit transaction"
            );
            return transactionRepository.save(newtransaction);
            }
            else {
                throw new ServiceException("Insufficient funds");
        }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionWithdraw(Transaction transaction) {
        try {
            Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    transaction.getRecieverIban(),
                    transaction.getAmount(),
                    "withdraw transaction"
            );
            return transactionRepository.save(newtransaction);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public void TransferMoney(Transaction transaction) {
        //TODO: add transfer money logic
        Account senderAccount = accountService.getAccountByIban(transaction.getSenderIban());
        Account receiverAccount = accountService.getAccountByIban(transaction.getRecieverIban());

        senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());

        // accountService.updateAccount(senderAccount);
        // accountService.updateAccount(receiverAccount);
    }


    public boolean CheckSufficientFunds(Transaction transaction) {
        try {
            if (transaction.getAmount() > accountService.getAccountByIban(transaction.getSenderIban()).getBalance()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
}
