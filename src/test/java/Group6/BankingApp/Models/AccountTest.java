package Group6.BankingApp.Models;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

    @Mock
    private User user;

    @Test
    public void testSetAndGetIban() {
        Account account = new Account();
        account.setIban("NL01INHO9501054837");
        String iban = account.getIban();

        assertEquals("NL01INHO9501054837", iban);
    }

    @Test
    public void testSetAndGetUser() {

        // Create an instance of the Account class
        Account account = new Account();
        account.setUser(user);
        User user2 = account.getUser();

        assertEquals(user, user2);
    }

}