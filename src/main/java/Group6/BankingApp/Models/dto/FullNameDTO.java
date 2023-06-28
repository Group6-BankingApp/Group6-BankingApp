package Group6.BankingApp.Models.dto;

public class FullNameDTO {

    private String firstName;
    private String lastName;

    public FullNameDTO() {
    }

    public FullNameDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
