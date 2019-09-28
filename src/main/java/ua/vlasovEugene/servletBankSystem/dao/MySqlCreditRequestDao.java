package ua.vlasovEugene.servletBankSystem.dao;

import ua.vlasovEugene.servletBankSystem.entity.CreditOpeningRequest;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MySqlCreditRequestDao implements ICreditRequestDao {
    private final String GET_REQUEST_BY_ID = "SELECT * FROM credit_opening_request WHERE request_ID = ?";
    private final String GET_ALL_USER_REQUESTS = "SELECT * FROM credit_opening_request";
    private final String DELETE_CURRENT_REQUEST = "DELETE FROM credit_opening_request WHERE request_ID = ?";
    private final String ADD_NEW_REQUEST = "INSERT INTO credit_opening_request VALUES (null,?,?,?,?)";


    @Override
    public List<CreditOpeningRequest> getAllUserRequests(Connection connection) throws SQLException {
        List<CreditOpeningRequest> requests = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USER_REQUESTS)) {

            while (resultSet.next()) {
                CreditOpeningRequest request = new CreditOpeningRequest(
                        resultSet.getInt("request_ID"),
                        resultSet.getString("user_email_login"),
                        resultSet.getBigDecimal("user_total_balance"),
                        resultSet.getBigDecimal("expected_credit_limit"),
                        resultSet.getTimestamp("date_of_end_credit").toLocalDateTime()
                );
                requests.add(request);
            }
        }
        return requests;
    }

    @Override
    public void addNewRequest(Connection connection, CreditOpeningRequest currentRequest) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ADD_NEW_REQUEST)) {
            statement.setString(1, currentRequest.getUserEmailLogin());
            statement.setBigDecimal(2, currentRequest.getUserTotalBalance());
            statement.setBigDecimal(3, currentRequest.getExpectedCreditLimit());
            statement.setTimestamp(4, Timestamp.valueOf(currentRequest.getDateOfEndCredit()));
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteCurrentRequest(Connection connection, CreditOpeningRequest currentRequest) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CURRENT_REQUEST)) {
            statement.setInt(1, currentRequest.getRequestId());
            statement.executeUpdate();
        }
    }

    @Override
    public CreditOpeningRequest getRequestById(Connection connection, Integer requestId) throws SQLException {
        CreditOpeningRequest result = new CreditOpeningRequest();

        try (PreparedStatement statement = connection.prepareStatement(GET_REQUEST_BY_ID)) {
            statement.setInt(1, requestId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = new CreditOpeningRequest(
                        resultSet.getInt("request_ID"),
                        resultSet.getString("user_email_login"),
                        resultSet.getBigDecimal("user_total_balance"),
                        resultSet.getBigDecimal("expected_credit_limit"),
                        resultSet.getTimestamp("date_of_end_credit").toLocalDateTime()
                );
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(GET_REQUEST_BY_ID, GET_ALL_USER_REQUESTS, DELETE_CURRENT_REQUEST, ADD_NEW_REQUEST);
    }
}
