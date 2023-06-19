package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.TransactionDTO;
import Group6.BankingApp.Services.AccountService;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import org.hibernate.annotations.Check;
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

    public Transaction addTransaction(TransactionDTO transaction) {
        try {
            if((CheckSufficientFunds(transaction, transaction.getPin())) && (CheckDailyLimit(accountService.getAccountByIban(transaction.getSenderIban()), transaction))){
                TransferMoney(transaction);
                Transaction newTransaction = new Transaction(transaction);
                    return transactionRepository.save(newTransaction);
                }
                else {
                    throw new ServiceException("Insufficient funds or daily limit reached");
                }
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionDeposit(Transaction transaction) {
        try {
            DespositMoney(transaction);
            Transaction newtransaction = new Transaction(
                    "cash",
                    transaction.getReceiverIban(),
                    transaction.getAmount()
            );
            return transactionRepository.save(newtransaction);
        } catch (Exception ex) {
            throw new ServiceException("Failed to add account", ex);
        }
    }
    public Transaction addTransactionWithdraw(TransactionDTO transaction, String pin) {
        try {
            if(CheckSufficientFunds(transaction, pin)){
            WithdrawMoney(transaction);
            Transaction newtransaction = new Transaction(
                    transaction.getSenderIban(),
                    "cash",
                    transaction.getAmount()
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
    public void TransferMoney(TransactionDTO transaction) {
        //TODO: add transfer money logic
        AccountDTO senderAccount = accountService.getAccountByIban(transaction.getSenderIban());
        AccountDTO receiverAccount = accountService.getAccountByIban(transaction.getReceiverIban());

        if((senderAccount.getUser().getId() != receiverAccount.getUser().getId())&&!((senderAccount.getAccountType().equals("Current"))&&(receiverAccount.getAccountType().equals("Current")))){
            throw new ServiceException("Transfer between different account types for different users is not allowed");
        }
        else if(senderAccount.getTransactionLimit()<transaction.getAmount()){
            throw new ServiceException("Cannot transfer more than "+senderAccount.getTransactionLimit()+" per transaction");
        }
        else{
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
            accountService.updateAccountByIban(transaction.getSenderIban(), senderAccount);
            accountService.updateAccountByIban(transaction.getReceiverIban(), receiverAccount);
        }
    }

    public void DespositMoney(Transaction transaction) {
        //TODO: add transfer money logic
        AccountDTO receiverAccount = accountService.getAccountByIban(transaction.getReceiverIban());
        if(receiverAccount.getAccountType() == "Current")    {
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
            accountService.updateAccountByIban(transaction.getReceiverIban(), receiverAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }
    public void WithdrawMoney(TransactionDTO transaction) {
        AccountDTO senderAccount = accountService.getAccountByIban(transaction.getSenderIban());
        if(senderAccount.getAccountType() == "Current")    {
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            accountService.updateAccountByIban(transaction.getSenderIban(), senderAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }


    public boolean CheckSufficientFunds(TransactionDTO transaction, String pin) {
        try {
            double absolutelimit=(accountService.getAccountByIban(transaction.getSenderIban())).getAbsoluteLimit();
            if ((accountService.getAccountBalance(transaction.getSenderIban(), pin) - transaction.getAmount()) >=absolutelimit ) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new ServiceException("Checking Transaction Failed", ex);
        }
    }

    public boolean CheckDailyLimit(AccountDTO account, TransactionDTO transaction){
        List<Transaction> dailyTransactions = findAllTransactions(0, 50, LocalDate.now().toString(), LocalDate.now().toString(), account.getIban(), account.getPin());
        //check if the sum of the transaction and the daily transactions is less than the daily limit
        double sum = transaction.getAmount();
        for(Transaction t : dailyTransactions){
            sum += t.getAmount();
        }
        if(sum > account.getDailyLimit()){
            return false;
        }
        else{
            return true;
        }
    }

    public List<Transaction> findAllTransactions(Integer skip, Integer limit, String dateFrom, String dateTo, String iban, String pin) {
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
            String senderIban = transaction.getSenderIban();
            if ((transactionDate.isAfter(fromDate) || transactionDate.isEqual(fromDate)) && 
            ((transactionDate.isBefore(toDate) || transactionDate.isEqual(toDate))) && (senderIban.equals(iban))) {
                    transactionsResult.add(transaction);
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

    public  List<Transaction> findAllTransactions(String iban) {
        try {
            Iterable<Transaction> allTransactions = transactionRepository.findAllBySenderIbanOrReceiverIban(iban, iban);
            return (List<Transaction>) allTransactions;
            } catch (Exception ex) {
                throw new ServiceException("Failed to retrieve transactions here", ex);
            }
        }

}
