package ua.vlasovEugene.servletBankSystem.dao;

import ua.vlasovEugene.servletBankSystem.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class MySqlUserDao implements IUserDao {

    private final String FIND_USER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM user WHERE user_login_email = ? " +
            "AND user_password = ?";
    private final String FIND_USER_BY_EMAIL = "SELECT * FROM user WHERE user_login_email = ?";
    private final String ADD_NEW_USER = "INSERT INTO user VALUES (null,?,?,?,?,?,?,?)";
    private final String CHANGE_CREDIT_REQUEST_STATUS = "UPDATE user SET credit_request_status = ? " +
            "WHERE user_login_email = ?";
    private final String CHANGE_CREDIT_ACCOUNT_STATUS = "UPDATE user SET user_credit_acc = ? " +
            "WHERE user_login_email = ?";


    @Override
    public boolean userByEmailIsExist(Connection connection, String login, String password) throws SQLException {
        boolean result = false;

        try (PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL_AND_PASSWORD)){
            statement.setString(1,login);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result = true;
            }
        }
        return result;
    }

    @Override
    public User getUserByLoginAndPassword(Connection connection, User user) throws SQLException {
        User currentUser = null;

        try(PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL_AND_PASSWORD)){
            statement.setString(1,user.getUserLoginEmail());
            statement.setString(2,user.getUserPassword());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                currentUser = new User();
                currentUser.setUserId(resultSet.getInt("user_ID"));
                currentUser.setUserFirstname(resultSet.getString("user_firstname"));
                currentUser.setUserLastname(resultSet.getString("user_secondname"));
                currentUser.setUserLoginEmail(resultSet.getString("user_login_email"));
                currentUser.setUserRole(resultSet.getString("user_role"));
                currentUser.setUserHaveCreditAcc(resultSet.getBoolean("user_credit_acc"));
                currentUser.setCreditRequestStatus(resultSet.getBoolean("send_credit_request"));
            }
        }
        return currentUser;
    }

    @Override
    public void addNewUser(Connection connection, User newUser) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(ADD_NEW_USER)){
            statement.setString(1,newUser.getUserFirstname());
            statement.setString(2,newUser.getUserLastname());
            statement.setString(3,newUser.getUserLoginEmail());
            statement.setString(4,newUser.getUserPassword());
            statement.setString(5,newUser.getUserRole());
            statement.setBoolean(6,newUser.getUserHaveCreditAcc());
            statement.setBoolean(7, newUser.getCreditRequestStatus());
            statement.executeUpdate();
        }
    }

    @Override
    public User getUserByLogin(Connection connection,String login) throws SQLException {
        User user = null;

        try(PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL)){
            statement.setString(1,login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("user_firstname"),
                        resultSet.getString("user_secondname"),
                        resultSet.getString("user_login_email"),
                        resultSet.getString("user_password"),
                        resultSet.getString("user_role"),
                        resultSet.getBoolean("user_credit_acc"),
                        resultSet.getBoolean("credit_request_status")
                );
            }
        }
        return user;
    }

    @Override
    public void changeCreditRequestStatusOfCurrentUser(Connection connection, String userLoginEmail, Boolean requestStatusFlag)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHANGE_CREDIT_REQUEST_STATUS)) {
            statement.setBoolean(1, requestStatusFlag);
            statement.setString(2, userLoginEmail);
            statement.executeUpdate();
        }
    }

    @Override
    public void changeCreditStatusOfCurrentUser(Connection connection, String userLoginEmail, Boolean isUserHaveCreditAcc) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHANGE_CREDIT_ACCOUNT_STATUS)) {
            statement.setBoolean(1, isUserHaveCreditAcc);
            statement.setString(2, userLoginEmail);
            statement.executeUpdate();
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (this == o) result = true;
        if (o == null || getClass() != o.getClass()) return false;
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FIND_USER_BY_EMAIL_AND_PASSWORD, FIND_USER_BY_EMAIL, ADD_NEW_USER);
    }
}
