package ua.vlasovEugene.servletBankSystem.entity;

import java.math.BigInteger;
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
    private String salt;

    private User() {
  }

    public void setCreditRequestStatus(Boolean creditRequestStatus) {
      this.creditRequestStatus = creditRequestStatus;
  }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getUserId() {
    return userId;
  }

    public String getUserFirstname() {
    return userFirstname;
  }

    public String getUserLastname() {
    return userLastname;
  }

    public String getUserLoginEmail() {
    return userLoginEmail;
  }

    public String getUserPassword() {
    return userPassword;
  }

    public String getUserRole() {
    return userRole;
  }

    public Boolean getUserHaveCreditAcc() {
    return userHaveCreditAcc;
  }

    public Boolean getCreditRequestStatus() {
        return creditRequestStatus;
    }

    public String getSalt() {
        return salt;
    }

    public static Builder newBuilder() {
        return new User().new Builder();
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
            Objects.equals(creditRequestStatus, user.creditRequestStatus) &&
            Objects.equals(salt, user.salt);
  }

  @Override
  public int hashCode() {
      return Objects.hash(userId, userFirstname, userLastname, userLoginEmail,
              userPassword, userRole, userHaveCreditAcc, creditRequestStatus, salt);
  }

    public class Builder {
        private Builder() {
        }

        public Builder setUserId(Integer userId) {
            User.this.userId = userId;
            return this;
        }

        public Builder setUserFirstname(String userFirstname) {
            User.this.userFirstname = userFirstname;
            return this;
        }

        public Builder setUserLastname(String userLastname) {
            User.this.userLastname = userLastname;
            return this;
        }

        public Builder setUserLoginEmail(String userLoginEmail) {
            User.this.userLoginEmail = userLoginEmail;
            return this;
        }

        public Builder setUserPassword(String userPassword) {
            User.this.userPassword = userPassword;
            return this;
        }

        public Builder setUserRole(String userRole) {
            User.this.userRole = userRole;
            return this;
        }

        public Builder setUserSalt(String salt) {
            User.this.salt = salt;
            return this;
        }

        public Builder setUserHaveCreditAcc(Boolean userHaveCreditAcc) {
            User.this.userHaveCreditAcc = userHaveCreditAcc;
            return this;
        }

        public Builder setCreditRequestStatus(Boolean creditRequestStatus) {
            User.this.creditRequestStatus = creditRequestStatus;
            return this;
        }

        public User build() {
            return User.this;
        }
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
                ", salt='" + salt + '\'' +
                '}';
    }
}

