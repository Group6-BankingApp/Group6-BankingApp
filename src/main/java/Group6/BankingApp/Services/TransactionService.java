package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import java.util.List;
import java.util.*;
import java.time.LocalDate;
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
            if(CheckSufficientFunds(transaction, "1234")){
                TransferMoney(transaction);
                Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    transaction.getreceiverIban(),
                    transaction.getAmount(),
                    "regular transaction"
                    );
                    return transactionRepository.save(newtransaction);
                }
                else {
                    throw new ServiceException("Transaction Failed");
                }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionDeposit(Transaction transaction) {
        try {
            //TODO: add pin to transfer
            DespositMoney(transaction);
            Transaction newtransaction = new Transaction(
                    "cash",
                    transaction.getreceiverIban(),
                    transaction.getAmount(),
                    "deposit transaction"
            );
            return transactionRepository.save(newtransaction);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionWithdraw(Transaction transaction) {
        try {
            if(CheckSufficientFunds(transaction, "1234")){
            WithdrawMoney(transaction);
            Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    "cash",
                    transaction.getAmount(),
                    "withdraw transaction"
            );
            return transactionRepository.save(newtransaction);
            }
            else {
                    throw new ServiceException("Transaction Failed");
            }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public void TransferMoney(Transaction transaction) {
        //TODO: add transfer money logic
        AccountDTO senderAccount = accountService.getAccountByIban(transaction.getSenderIban());
        AccountDTO receiverAccount = accountService.getAccountByIban(transaction.getreceiverIban());
        if(senderAccount.getAccountType() == "Current" && receiverAccount.getAccountType() == "Current")    {
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
            accountService.updateAccountByIban(transaction.getSenderIban(), senderAccount);
            accountService.updateAccountByIban(transaction.getreceiverIban(), receiverAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }

    public void DespositMoney(Transaction transaction) {
        //TODO: add transfer money logic
        AccountDTO receiverAccount = accountService.getAccountByIban(transaction.getreceiverIban());
        if(receiverAccount.getAccountType() == "Current")    {
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
            accountService.updateAccountByIban(transaction.getreceiverIban(), receiverAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }
    public void WithdrawMoney(Transaction transaction) {
        AccountDTO senderAccount = accountService.getAccountByIban(transaction.getSenderIban());
        if(senderAccount.getAccountType() == "Current")    {
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            accountService.updateAccountByIban(transaction.getSenderIban(), senderAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }


    public boolean CheckSufficientFunds(Transaction transaction, String pin) {
        try {
            if ((accountService.getAccountBalance(transaction.getSenderIban(), pin) - transaction.getAmount()) >= accountService.getAccountByIban(transaction.getSenderIban()).getAbsoluteLimit()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new ServiceException("Transaction Failed", ex);
        }
    }

    public boolean CheckDailyLimit(Account account, Transaction transaction){
        //TODO: check if user has reached their limit for the day (maybe do this in check sufficient funds?)
        return true;
    }

    public List<Transaction> findAllTransactions(Integer skip, Integer limit, String dateFrom, String dateTo) {
    try {
        Iterable<Transaction> allTransactions = transactionRepository.findAll();
        if (allTransactions == null)
            throw new ServiceException("Failed to retrieve accounts");

        List<Transaction> transactionsList = new ArrayList<>();
        allTransactions.forEach(transactionsList::add);

        // Convert dateFrom and dateTo strings to LocalDate objects
        LocalDate fromDate = LocalDate.parse(dateFrom);
        LocalDate toDate = LocalDate.parse(dateTo);

        List<Transaction> transactionsResult = new ArrayList<>();
        for (Transaction transaction : transactionsList) {
            LocalDate transactionDate = transaction.getTimeCreated();
            if (transactionDate.isAfter(fromDate) || transactionDate.isEqual(fromDate)) {
                if (transactionDate.isBefore(toDate) || transactionDate.isEqual(toDate)) {
                    transactionsResult.add(transaction);
                }
            }
        }

        int totalTransactions = transactionsResult.size();

        if (skip >= totalTransactions)
            return new ArrayList<>();

        int end = Math.min(skip + limit, totalTransactions);
        return transactionsResult.subList(skip, end);
    } catch (Exception ex) {
        throw new ServiceException("Failed to retrieve transactions here", ex);
    }
}

}
