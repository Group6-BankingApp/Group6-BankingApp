package Group6.BankingApp.Models.dto;

public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
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

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
