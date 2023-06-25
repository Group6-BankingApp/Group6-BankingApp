package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.TransactionRepository;
import Group6.BankingApp.Models.Transaction;
import Group6.BankingApp.Models.dto.*;
import Group6.BankingApp.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Transaction addTransfer(TransactionDTO transactionDTO){
        try {
            if(!transactionDTO.getSenderIban().equals(transactionDTO.getReceiverIban())){
                Account senderAccount = accountService.findAccountByIban(transactionDTO.getSenderIban());
                Account receiverAccount = accountService.findAccountByIban(transactionDTO.getReceiverIban());
                if((senderAccount.getBalance()- senderAccount.getAbsoluteLimit())<transactionDTO.getAmount()){
                    throw new ServiceException("Insufficient funds");
                }
                if(senderAccount.getUser().getId()!= receiverAccount.getUser().getId()){
                    if(!(senderAccount.getAccountType().equals("Current") && receiverAccount.getAccountType().equals("Current"))){
                        throw new ServiceException("Cross-account transfers are only allowed between current accounts");
                    }
                    if (transactionDTO.getAmount()> senderAccount.getTransactionLimit()){
                        throw new ServiceException("Transaction limit exceeded");
                    }
                    else if(transactionDTO.getAmount()> senderAccount.getDailyLimit()){
                        throw new ServiceException("Daily limit reached");
                    }
                    else{
                        senderAccount.setDailyLimit(senderAccount.getDailyLimit()-transactionDTO.getAmount());
                    }
                }
                senderAccount.setBalance(senderAccount.getBalance()-transactionDTO.getAmount());
                receiverAccount.setBalance(receiverAccount.getBalance()+transactionDTO.getAmount());
                accountService.updateAccountByIban(senderAccount.getIban(), senderAccount);
                accountService.updateAccountByIban(receiverAccount.getIban(), receiverAccount);
                Transaction newTransaction = new Transaction(transactionDTO);
                return transactionRepository.save(newTransaction);
            }
            else {
                throw new ServiceException("Sender and receiver cannot be the same");
            }
        }catch (Exception ex){
            throw new ServiceException("Failed to add transaction. ", ex);
        }
    }
//    public Transaction addTransaction(TransactionDTO transaction) {
//        try {
//            if((CheckSufficientFunds(transaction, transaction.getPin())) && (CheckDailyLimit(accountService.getAccountByIban(transaction.getSenderIban()), transaction))){
//                TransferMoney(transaction);
//                Transaction newTransaction = new Transaction(transaction);
//                    return transactionRepository.save(newTransaction);
//                }
//                else {
//                    throw new ServiceException("Insufficient funds or daily limit reached");
//                }
//        } catch (Exception ex) {
//            throw new ServiceException("Failed to add transaction", ex);
//        }
//    }
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
        Account senderAccount = accountService.findAccountByIban(transaction.getSenderIban());
        Account receiverAccount = accountService.findAccountByIban(transaction.getReceiverIban());

        if((senderAccount.getUser().getId() != receiverAccount.getUser().getId())&&!((senderAccount.getAccountType().equals("Current"))&&(receiverAccount.getAccountType().equals("Current")))){
            throw new ServiceException("Transfer between different account types for different users is not allowed");
        }
        else if(senderAccount.getTransactionLimit()<=transaction.getAmount()){
            throw new ServiceException("Cannot transfer more than "+ senderAccount.getTransactionLimit()+" per transaction");
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
        Account receiverAccount = accountService.findAccountByIban(transaction.getReceiverIban());
        if(receiverAccount.getAccountType() == "Current")    {
            receiverAccount.setBalance(receiverAccount.getBalance() + transaction.getAmount());
            accountService.updateAccountByIban(transaction.getReceiverIban(), receiverAccount);
        }
        else{
            throw new ServiceException("Invalid Account for Transfer");
        }
    }
    public void WithdrawMoney(TransactionDTO transaction) {
        Account senderAccount = accountService.findAccountByIban(transaction.getSenderIban());
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

    public AtmResponseDTO makeWithdraw(AtmTransactionDTO transaction) {
        try {
            Account account = accountService.findAccountByIban(transaction.getIban());
            Double amount = Double.parseDouble(transaction.getAmount());
            if(account.getAccountType().equals("Current")){
                if((account.getBalance()-account.getAbsoluteLimit())>=amount){
                    Transaction newtransaction = new Transaction(transaction.getIban() ,"NL01INHO0000000001", amount);
                    newtransaction.setUserId(account.getUser().getId());
                    newtransaction.setTimeCreated(LocalDate.now());
                    transactionRepository.save(newtransaction);
                    account.setBalance(account.getBalance()-amount);
                    account.setDailyLimit(account.getDailyLimit()-amount);
                    accountService.updateAccountByIban(transaction.getIban(), account);
                    String balance = Double.toString(account.getBalance());
                    AtmResponseDTO response = new AtmResponseDTO(account.getUser().getFirstName() + " " + account.getUser().getLastName(), account.getCardNumber(),account.getIban(), balance);
                    return response;
                }
                else{
                    throw new ServiceException("Insufficient funds");
                }
            }
            else{
                throw new ServiceException("Invalid account type");
            }
        } catch (Exception ex) {
            throw new ServiceException("Failed to withdraw", ex);
        }
    }

    public AtmResponseDTO makeDeposit(AtmTransactionDTO transaction) {
        try {
            Account account = accountService.findAccountByIban(transaction.getIban());
            Double amount = Double.parseDouble(transaction.getAmount());
            if(account.getAccountType().equals("Current")){
                Transaction newtransaction = new Transaction("NL01INHO0000000001", transaction.getIban() ,amount);
                newtransaction.setUserId(account.getUser().getId());
                newtransaction.setTimeCreated(LocalDate.now());
                transactionRepository.save(newtransaction);
                account.setBalance(account.getBalance()+amount);
                String balance = Double.toString(account.getBalance());
                accountService.updateAccountByIban(transaction.getIban(), account);
                AtmResponseDTO response = new AtmResponseDTO(account.getUser().getFirstName()+" "+account.getUser().getLastName(), account.getCardNumber(), account.getIban(), balance);

                return response;
            }
            else{
                throw new ServiceException("Invalid account type");
            }
        } catch (Exception ex) {
            throw new ServiceException("Failed to deposit", ex);
        }
    }

    public List<Transaction> findTransactionsByFilter(String iban, FilterDTO filter) {
        filter=checkFilter(filter);
        try{
            Iterable<Transaction> transactions = null;
            List<Transaction> filteredTransactions = null;
            if((filter.getAccount()==null)||(filter.getAccount().equals(""))){
                if((filter.getFromOrTo()==null)||(filter.getFromOrTo().equals(""))){
                    transactions = transactionRepository.findAll();
                    filteredTransactions = applyFilters(transactions, filter);
                }
                else{
                    if(filter.getFromOrTo().equals("from")){
                        transactions = transactionRepository.findAllByReceiverIban(iban);
                        filteredTransactions = applyFilters(transactions, filter);
                    }
                    else{
                        transactions = transactionRepository.findAllBySenderIban(iban);
                        filteredTransactions = applyFilters(transactions, filter);
                    }
                }
            }
            else{
                if((filter.getFromOrTo()==null)||(filter.getFromOrTo().equals(""))){
                    transactions = transactionRepository.findAllBySenderIbanAndReceiverIban(iban, filter.getAccount());
                    Iterable<Transaction> transactions2 = transactionRepository.findAllBySenderIbanAndReceiverIban(filter.getAccount(), iban);
                    filteredTransactions = applyFilters(transactions, filter);
                    List<Transaction> filtered2 = applyFilters(transactions2, filter);
                    filteredTransactions.addAll(filtered2);
                }
                else{
                    if(filter.getFromOrTo().equals("from")){
                        transactions = transactionRepository.findAllBySenderIbanAndReceiverIban(filter.getAccount(), iban);
                        filteredTransactions = applyFilters(transactions, filter);
                    }
                    else{
                        transactions = transactionRepository.findAllBySenderIbanAndReceiverIban(iban, filter.getAccount());
                        filteredTransactions = applyFilters(transactions, filter);
                    }
                }
            }
            return  filteredTransactions;
        }
        catch(Exception ex){
            throw new ServiceException("Failed to retrieve transactions", ex);
        }
    }

    private FilterDTO checkFilter(FilterDTO filter) {
        if(filter.getMinAmount()==null){
            filter.setMinAmount(0.0);
        }
        if(filter.getMaxAmount()==null){
            filter.setMaxAmount(100000000.0);
        }
        if(filter.getStartDate()==null){
            filter.setStartDate(LocalDate.of(1900, 1, 1));
        }
        if(filter.getEndDate()==null){
            filter.setEndDate(LocalDate.now());
        }
        return filter;
    }

    private List<Transaction> applyFilters(Iterable<Transaction> transactions, FilterDTO filter) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if((transaction.getTimeCreated().isAfter(filter.getStartDate()) && transaction.getTimeCreated().isBefore(filter.getEndDate()))
                    &&((transaction.getAmount()<=filter.getMaxAmount())&&(transaction.getAmount()>=filter.getMinAmount()))){
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> findAll() {
try {
            Iterable<Transaction> allTransactions = transactionRepository.findAll();
            return (List<Transaction>) allTransactions;
        } catch (Exception ex) {
            throw new ServiceException("Failed to retrieve transactions here", ex);
        }
    }
}
