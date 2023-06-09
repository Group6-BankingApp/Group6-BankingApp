package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.Customer;

public class UserDTO2 {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public UserDTO2(){}

    public UserDTO2(Customer customer) {
        this.id = customer.getId();
        this.firstName =  customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
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
}
