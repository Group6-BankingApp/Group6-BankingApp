package Group6.BankingApp.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountClosed {

    private String iban;
    private String firstname;
    private String lastname;
    private String status;
    private String type;
    private BigDecimal balance;

    public AccountClosed() {
    }

    public AccountClosed(String iban, String firstname, String lastname, String status, String type, BigDecimal balance) {
        this.iban = iban;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.type = type;
        this.balance = balance;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AccountClosed{");
        sb.append("iban='").append(iban).append('\'');
        sb.append(", firstname='").append(firstname).append('\'');
        sb.append(", lastname='").append(lastname).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}
