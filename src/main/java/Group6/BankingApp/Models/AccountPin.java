package Group6.BankingApp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AccountPin {

    @Id
    private String code;

    public AccountPin() {
    }

    public AccountPin(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
