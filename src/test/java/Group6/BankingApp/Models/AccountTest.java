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

//    @Test
//    void testMapToUser() {
//
//        UserDTO2 userDTO2 = new UserDTO2();
//        userDTO2.setId(1L);
//        userDTO2.setFirstName("Daniel");
//        userDTO2.setLastName("Rozenhart");
//        userDTO2.setEmail("daniel.roz@email.com");
//        userDTO2.setPhoneNumber("0614987314");
//
//        // Call the mapToUser method
//        Account account = new Account();
//        User user = account.mapToUser(userDTO2);
//
//        // Verify the mapped user properties
//        Assertions.assertEquals(userDTO2.getId(), user.getId());
//        Assertions.assertEquals(userDTO2.getFirstName(), user.getFirstName());
//        Assertions.assertEquals(userDTO2.getLastName(), user.getLastName());
//        Assertions.assertEquals(userDTO2.getEmail(), user.getEmail());
//        Assertions.assertEquals(userDTO2.getPhoneNumber(), user.getPhoneNumber());
//    }

}