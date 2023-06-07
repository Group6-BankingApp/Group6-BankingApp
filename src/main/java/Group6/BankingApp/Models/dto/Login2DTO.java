package Group6.BankingApp.Models.dto;

import Group6.BankingApp.Models.User;

public class Login2DTO {
    private String username;
    private String password;
    private String role;

    public Login2DTO() {
    }

    public Login2DTO(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        if(user.getRole() != null)
            this.role = user.getRole().toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
