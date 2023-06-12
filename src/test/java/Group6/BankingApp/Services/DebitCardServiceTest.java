package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DebitCardServiceTest {

    @Mock
    private DebitCardRepository debitCardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DebitCardService debitCardService;

    @Test
    public void testCreateDebitCard() {

        
        Account account = new Account();
        account.setIban("NL01INHO0000000001");
        account.setCardUUID("some_uuid");
        account.setPin("1234");
        account.setBalance(1000.0);


        DebitCard existingActiveCard = new DebitCard();
        existingActiveCard.setCardNumber("6638545966347381");
        existingActiveCard.setExpirationDate(LocalDate.now().plusYears(5));
        existingActiveCard.setActive(true);
        existingActiveCard.setUuid(account.getCardUUID());
        existingActiveCard.setAccount(account);


        DebitCard newCard = new DebitCard();
        newCard.setCardNumber("5538545966347381");
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(false);
        newCard.setUuid(account.getCardUUID());
        newCard.setAccount(account);


        DebitCard savedCard = new DebitCard();
        savedCard.setId(1L);
        savedCard.setCardNumber(newCard.getCardNumber());
        savedCard.setExpirationDate(newCard.getExpirationDate());
        savedCard.setActive(newCard.isActive());
        savedCard.setUuid(newCard.getUuid());
        savedCard.setAccount(newCard.getAccount());


        Mockito.when(debitCardRepository.findByAccountAndIsActive(account, true)).thenReturn(existingActiveCard);
        Mockito.when(debitCardRepository.save(existingActiveCard)).thenReturn(existingActiveCard);
        Mockito.when(debitCardRepository.save(newCard)).thenReturn(savedCard);


        Mockito.when(accountRepository.save(account)).thenReturn(account);


        DebitCardDTO result = debitCardService.createDebitCard(account);


        assertEquals(savedCard.getCardNumber(), result.getCardNumber());


        Mockito.verify(debitCardRepository, Mockito.times(1)).findByAccountAndIsActive(account, true);
        Mockito.verify(debitCardRepository, Mockito.times(1)).save(existingActiveCard);
        Mockito.verify(debitCardRepository, Mockito.times(1)).save(newCard);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);

    }
}
