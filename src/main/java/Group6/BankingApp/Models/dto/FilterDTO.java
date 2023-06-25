package Group6.BankingApp.Models.dto;

import java.time.LocalDate;

public class FilterDTO {

    private String startDate;
    private String endDate;
    private Double minAmount;
    private Double maxAmount;
    private String account;
    private String fromOrTo;

    public FilterDTO() {
    }

    public FilterDTO(String startDate, String endDate, Double minAmount, Double maxAmount, String account, String fromOrTo) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.account = account;
        this.fromOrTo = fromOrTo;
    }

    public LocalDate getStartDate() {
        return LocalDate.parse(startDate);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate.toString();
    }

    public LocalDate getEndDate() {
        return LocalDate.parse(endDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate.toString();
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFromOrTo() {
        return fromOrTo;
    }

    public void setFromOrTo(String fromOrTo) {
        this.fromOrTo = fromOrTo;
    }
}
