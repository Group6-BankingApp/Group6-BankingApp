package Group6.BankingApp.Models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean hasCurrentAccount;
    private boolean hasSavingsAccount;

    @ElementCollection (fetch = FetchType.EAGER)
    private List<Role> roles;

    public boolean isHasCurrentAccount() {
        return hasCurrentAccount;
    }
    public void setHasCurrentAccount(boolean hasCurrentAccount) {
        this.hasCurrentAccount = hasCurrentAccount;
    }

    public boolean isHasSavingsAccount() {
        return hasSavingsAccount;
    }
    public void setHasSavingsAccount(boolean hasSavingsAccount) {
        this.hasSavingsAccount = hasSavingsAccount;
    }
}
