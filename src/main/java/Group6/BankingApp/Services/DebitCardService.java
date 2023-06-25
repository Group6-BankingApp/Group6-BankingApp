package Group6.BankingApp.Services;

import Group6.BankingApp.DAL.AccountRepository;
import Group6.BankingApp.DAL.DebitCardRepository;
import Group6.BankingApp.Models.Account;
import Group6.BankingApp.Models.DebitCard;
import Group6.BankingApp.Models.dto.AccountDTO;
import Group6.BankingApp.Models.dto.AtmLoginDTO;
import Group6.BankingApp.Models.dto.AtmResponseDTO;
import Group6.BankingApp.Models.dto.DebitCardDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DebitCardService {

    private final DebitCardRepository debitCardRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public DebitCardService(DebitCardRepository debitCardRepository, AccountRepository accountRepository, AccountService accountService) {
        this.debitCardRepository = debitCardRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    public List<DebitCardDTO> getAllDebitCards() {
        List<DebitCard> debitCards = (List<DebitCard>) debitCardRepository.findAll();
        List<DebitCardDTO> debitCardDTOs = new ArrayList<>();
        for (DebitCard debitCard: debitCards) {
            debitCardDTOs.add(mapToDebitCardDTO(debitCard));
        }
        return debitCardDTOs;
    }

    public  DebitCardDTO getDebitCardByUUID(String uuid) {
        DebitCard debitCard = debitCardRepository.findByUuid(uuid);
        return mapToDebitCardDTO(debitCard);
    }

    public DebitCardDTO createDebitCard(Account account) {
        if(!"Current".equals(account.getAccountType())){
            throw new ServiceException("Debit card can only be created for 'Current' accounts!");
        }
        DebitCard existingActiveCard = debitCardRepository.findByAccountAndIsActive(account, true);
        if (existingActiveCard != null) {
            throw new ServiceException("Account already has an active debit card!");
        }

        DebitCard newCard = new DebitCard();
        newCard.setCardNumber(generateDebitCardNumber());
        newCard.setExpirationDate(LocalDate.now().plusYears(5));
        newCard.setActive(true);
        newCard.setUuid(generateCardUUID());
        newCard.setAccount(account);

        debitCardRepository.save(newCard);
        account.setHasCard(true);
        account.setCardNumber(newCard.getCardNumber());
        accountRepository.save(account);
        return mapToDebitCardDTO(newCard);
    }

    public void deactivatePreviousCard(Account account) {
        DebitCard previousCard = debitCardRepository.findByAccountAndIsActive(account, true);
        if (previousCard != null) {
            previousCard.setActive(false);
            debitCardRepository.save(previousCard);
        }
    }

    public DebitCardDTO activateDebitCard(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuidAndIsActive(cardUUID, false);
        if(card == null && card.getAccount().getPin() != pin)
            return null;

        deactivatePreviousCard(card.getAccount());
        card.setActive(true);
        DebitCard savedCard = debitCardRepository.save(card);
        return mapToDebitCardDTO(savedCard);
    }

    public void deactivateDebitCard(String iban, String cardNumber, boolean active) {
        try {
            Account account = accountRepository.findByIban(iban);
            if (account == null) {
                throw new ServiceException("Invalid IBAN");
            }

            DebitCard debitCard = debitCardRepository.findByAccountAndCardNumber(account, cardNumber);
            if (debitCard == null) {
                throw new ServiceException("Invalid debit card number");
            }

            debitCard.setActive(active);
            debitCardRepository.save(debitCard);
        } catch (Exception ex) {
            throw new ServiceException("Failed to deactivate debit card", ex);
        }
    }

    public boolean verifyCardCredentials(String cardUUID, String pin) {
        DebitCard card = debitCardRepository.findByUuid(cardUUID);
        return card != null && card.getAccount().getPin().equals(pin);
    }



    public String generateDebitCardNumber() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        sb.append("4");
        for (int i = 0; i < 3; i++)
            sb.append(random.nextInt(10));

        for (int i = 0; i < 12; i++)
            sb.append(random.nextInt(10));

        return sb.toString();
    }

    protected DebitCardDTO mapToDebitCardDTO(DebitCard card) {
        if(card == null)
            throw new ServiceException("card does not exist.");

        String date = card.getExpirationDate().toString();
        DebitCardDTO cardDTO = new DebitCardDTO(card.getCardNumber(),card.getUuid(),date,card.isActive());
        return cardDTO;
    }

    public String generateCardUUID() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public Object insertDebitCard(AtmLoginDTO atmLoginDTO) {
        DebitCard debitcard=debitCardRepository.findByUuid(atmLoginDTO.getCardUUID());
        if(debitcard==null)
            throw new ServiceException("Invalid card UUID");
        if(!debitcard.isActive()){
            throw new ServiceException("Card is no longer active");
        }
        if(!(debitcard.getAccount().getPin().equals(atmLoginDTO.getPin()))){
            throw new ServiceException("Invalid pin");
        }
        String balance=Double.toString(debitcard.getAccount().getBalance());
        AtmResponseDTO atmResponseDTO=new AtmResponseDTO(debitcard.getAccount().getUser().getFirstName()+" "+debitcard.getAccount().getUser().getLastName(),debitcard.getCardNumber(),debitcard.getAccount().getIban(),balance);
        return atmResponseDTO;
    }
}
