package ua.vlasovEugene.servletBankSystem.entity;

import java.util.Objects;

/**
 * Entity Class of table 'user'
 * */

public class User {

  private Integer userId;
  private String userFirstname;
  private String userLastname;
  private String userLoginEmail;
  private String userPassword;
  private String userRole;
  private Boolean userHaveCreditAcc;
    private Boolean creditRequestStatus;

  public User() {
  }

  public User(Integer userId, String userFirstname, String userLastname, String userLoginEmail,
              String userPassword, String userRole, Boolean userHaveCreditAcc, Boolean creditRequestStatus) {
    this.userId = userId;
    this.userFirstname = userFirstname;
    this.userLastname = userLastname;
    this.userLoginEmail = userLoginEmail;
    this.userPassword = userPassword;
    this.userRole = userRole;
    this.userHaveCreditAcc = userHaveCreditAcc;
      this.creditRequestStatus = creditRequestStatus;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserFirstname() {
    return userFirstname;
  }

  public void setUserFirstname(String userFirstname) {
    this.userFirstname = userFirstname;
  }

  public String getUserLastname() {
    return userLastname;
  }

  public void setUserLastname(String userLastname) {
    this.userLastname = userLastname;
  }

  public String getUserLoginEmail() {
    return userLoginEmail;
  }

  public void setUserLoginEmail(String userLoginEmail) {
    this.userLoginEmail = userLoginEmail;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public Boolean getUserHaveCreditAcc() {
    return userHaveCreditAcc;
  }

  public void setUserHaveCreditAcc(Boolean userHaveCreditAcc) {
    this.userHaveCreditAcc = userHaveCreditAcc;
  }

    public Boolean getCreditRequestStatus() {
        return creditRequestStatus;
    }

    public void setCreditRequestStatus(Boolean creditRequestStatus) {
        this.creditRequestStatus = creditRequestStatus;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(userId, user.userId) &&
            Objects.equals(userFirstname, user.userFirstname) &&
            Objects.equals(userLastname, user.userLastname) &&
            Objects.equals(userLoginEmail, user.userLoginEmail) &&
            Objects.equals(userPassword, user.userPassword) &&
            Objects.equals(userRole, user.userRole) &&
            Objects.equals(userHaveCreditAcc, user.userHaveCreditAcc) &&
            Objects.equals(creditRequestStatus, user.creditRequestStatus);
  }

  @Override
  public int hashCode() {
      return Objects.hash(userId, userFirstname, userLastname, userLoginEmail,
              userPassword, userRole, userHaveCreditAcc, creditRequestStatus);
  }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userFirstname='" + userFirstname + '\'' +
                ", userLastname='" + userLastname + '\'' +
                ", userLoginEmail='" + userLoginEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userHaveCreditAcc=" + userHaveCreditAcc +
                ", creditRequestStatus=" + creditRequestStatus +
                '}';
  }
}
