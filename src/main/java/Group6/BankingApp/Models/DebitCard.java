package Group6.BankingApp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class DebitCard {

    @Id
    private UUID uuid;
    private String cardNumber;
    private LocalDate expirationDate;
    private String cvv;
    private String cardHolderName;
    private Boolean isActive;

    @ManyToOne
    private Account account;

    public DebitCard() {
    }

    public DebitCard(UUID uuid, String cardNumber, LocalDate expirationDate, String cvv, String cardHolderName, Boolean isActive, Account account) {
        this.uuid = uuid;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.cardHolderName = cardHolderName;
        this.isActive = isActive;
        this.account = account;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DebitCard{");
        sb.append("uuid=").append(uuid);
        sb.append(", cardNumber='").append(cardNumber).append('\'');
        sb.append(", expirationDate=").append(expirationDate);
        sb.append(", cvv='").append(cvv).append('\'');
        sb.append(", cardHolderName='").append(cardHolderName).append('\'');
        sb.append(", isActive=").append(isActive);
        sb.append(", account=").append(account);
        sb.append('}');
        return sb.toString();
    }
}
