package ua.vlasovEugene.servletBankSystem.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Entity Class of table 'account'
 * */
public class Account {

  private Integer accountId;
  private String accountOwner;
  private Long accountNumber;
  private String accountType;
  private BigDecimal currentBalance;
  private BigDecimal interestRate;
  private BigDecimal creditLimit;
  private LocalDateTime accountValidity;
    private BigDecimal deposit;

  public Account() {
  }

  public Account(Integer accountId, String accountOwner, Long accountNumber,
                 String accountType, BigDecimal currentBalance, BigDecimal interestRate,
                 BigDecimal creditLimit, LocalDateTime accountValidity, BigDecimal deposit) {
    this.accountId = accountId;
    this.accountOwner = accountOwner;
    this.accountNumber = accountNumber;
    this.accountType = accountType;
    this.currentBalance = currentBalance;
    this.interestRate = interestRate;
    this.creditLimit = creditLimit;
    this.accountValidity = accountValidity;
      this.deposit = deposit;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public String getAccountOwner() {
    return accountOwner;
  }

  public void setAccountOwner(String accountOwner) {
    this.accountOwner = accountOwner;
  }

  public Long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public BigDecimal getCurrentBalance() {
    return currentBalance;
  }

  public void setCurrentBalance(BigDecimal currentBalance) {
    this.currentBalance = currentBalance;
  }

  public BigDecimal getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(BigDecimal interestRate) {
    this.interestRate = interestRate;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }

  public LocalDateTime getAccountValidity() {
    return accountValidity;
  }

  public void setAccountValidity(LocalDateTime accountValidity) {
    this.accountValidity = accountValidity;
  }

  public BigDecimal getDeposit() {
        return deposit;
    }

  public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(accountId, account.accountId) &&
            Objects.equals(accountOwner, account.accountOwner) &&
            Objects.equals(accountNumber, account.accountNumber) &&
            Objects.equals(accountType, account.accountType) &&
            Objects.equals(currentBalance, account.currentBalance) &&
            Objects.equals(interestRate, account.interestRate) &&
            Objects.equals(creditLimit, account.creditLimit) &&
            Objects.equals(accountValidity, account.accountValidity) &&
            Objects.equals(deposit, account.deposit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, accountOwner, accountNumber, accountType, currentBalance, interestRate, creditLimit, accountValidity, deposit);
  }
}
