package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.User;

public class UserDTO2 {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String hasCurrentAccount;
    private String hasSavingsAccount;

    public UserDTO2(){}

    public UserDTO2(User user) {
        this.id = user.getId();
        this.firstName =  user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        if (user.isHasCurrentAccount()) {
            this.hasCurrentAccount = "Yes";
        } else {
            this.hasCurrentAccount = "No";
        }
        if (user.isHasSavingsAccount()) {
            this.hasSavingsAccount = "Yes";
        } else {
            this.hasSavingsAccount = "No";
        }
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHasCurrentAccount() {
        return hasCurrentAccount;
    }

    public String getHasSavingsAccount() {
        return hasSavingsAccount;
    }
}
