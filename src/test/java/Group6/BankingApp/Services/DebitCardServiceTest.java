package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DebitCardServiceTest {

    @Mock
    private DebitCardRepository debitCardRepository;

    @Mock
    private AccountRepository accountRepository;


    @InjectMocks
    @Autowired
    private AccountService accountService;

    @InjectMocks
    DebitCardService debitCardService;

    @Test
    void testCreateDebitCard() {

        Account account = new Account();

        DebitCard existingActiveCard = new DebitCard();
        existingActiveCard.setActive(true);

        DebitCard newCard = new DebitCard();
        newCard.setCardNumber("1234567890");
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(false);
        newCard.setUuid("1234567890");
        newCard.setAccount(account);

        when(debitCardRepository.findByAccountAndIsActive(account, true)).thenReturn(existingActiveCard);
        when(debitCardRepository.save(any(DebitCard.class))).thenReturn(newCard);

        // Invoke the method
        DebitCardDTO result = debitCardService.createDebitCard(account);

        // Verify the expected behavior
        verify(debitCardRepository, times(1)).findByAccountAndIsActive(account, true);
        verify(debitCardRepository, times(1)).save(newCard);
        verify(accountRepository, times(1)).save(account);

        assertFalse(existingActiveCard.isActive());
        assertEquals("1234567890", newCard.getCardNumber());
    }

//    @Test
//    void testMapToDebitCardDTO() {
//
//        DebitCard debitCard = new DebitCard();
//        debitCard.setCardNumber("1234567890");
//
//        DebitCardDTO debitCardDTO = debitCardService.mapToDebitCardDTO(debitCard);
//
//        assertNotNull(debitCardDTO);
//
//        assertEquals("1234567890", debitCardDTO.getCardNumber());
//    }
}
