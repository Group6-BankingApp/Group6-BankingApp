package Group6.BankingApp.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import Group6.BankingApp.Models.Transaction;

class TransactionTest {

    @Test
    void testConstructorAndGetters() {
        // Create a transaction
        String senderIban = "123456789";
        String receiverIban = "987654321";
        double amount = 100.0;
        String description = "Test transaction";
        Transaction transaction = new Transaction(senderIban, receiverIban, amount);
        
        // Verify the values are set correctly
        assertEquals(senderIban, transaction.getSenderIban());
        assertEquals(receiverIban, transaction.getReceiverIban());
        assertEquals(amount, transaction.getAmount());
        
        // Verify that the timeCreated is set to the current date
        assertEquals(LocalDate.now(), transaction.getTimeCreated());
    }
    
    @Test
    void testSetterAndGetters() {
        // Create a transaction
        Transaction transaction = new Transaction();
        
        // Set values using setters
        String senderIban = "123456789";
        String receiverIban = "987654321";
        double amount = 100.0;
        String description = "Test transaction";
        LocalDate timeCreated = LocalDate.of(2023, 1, 1);
        
        transaction.setSenderIban(senderIban);
        transaction.setReceiverIban(receiverIban);
        transaction.setAmount(amount);
        transaction.setTimeCreated(timeCreated);
        
        // Verify the values are set correctly
        assertEquals(senderIban, transaction.getSenderIban());
        assertEquals(receiverIban, transaction.getReceiverIban());
        assertEquals(amount, transaction.getAmount());
        assertEquals(timeCreated, transaction.getTimeCreated());
    }
}
