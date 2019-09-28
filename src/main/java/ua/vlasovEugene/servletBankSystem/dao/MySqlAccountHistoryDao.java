package ua.vlasovEugene.servletBankSystem.dao;

import ua.vlasovEugene.servletBankSystem.entity.PaymentHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * implementation of the interface for working with MySql table
 * @see ua.vlasovEugene.servletBankSystem.dao.IAccountHistoryDao
 */
public class MySqlAccountHistoryDao implements IAccountHistoryDao {
    private final String GET_TOTAL_COUNT_OF_RECORDS = "SELECT COUNT(*) FROM payment_history";
    private final String INSERT_NEW_ACTION_NOTE = "INSERT INTO payment_history VALUES (null,?,?,?,?,?)";
    private final String GET_ALL_ACTION_FOR_CURRENT_ACC = "SELECT * FROM payment_history WHERE account_number = ?" +
            " ORDER BY date_of_transaction DESC LIMIT ? OFFSET ?";

    /**
     * The method that creates an entry in the table "payment_history" when a new account is created
     * @param connection input parameter for working with sql database.
     * @param history entity class with changes to this account
     * @throws SQLException if something goes wrong this exception will be thrown
     */
    @Override
    public void addNewActionWithAccount(Connection connection, PaymentHistory history) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ACTION_NOTE)){
            statement.setLong(1,history.getAccountNumber());
            statement.setBigDecimal(2,history.getTransactionAmount());
            statement.setBigDecimal(3,history.getCurrentBalance());
            statement.setTimestamp(4,Timestamp.valueOf(history.getDateOfTransaction()));
            statement.setString(5,history.getNotification());
            statement.executeUpdate();
        }
    }

    @Override
    public List<PaymentHistory> getHistoryOfCurrentAccount(
            Connection connection, Long accountNumber, int ofset, int limit) throws SQLException {
        List<PaymentHistory> result = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(GET_ALL_ACTION_FOR_CURRENT_ACC)){
            statement.setLong(1,accountNumber);
            statement.setInt(2, limit);
            statement.setInt(3, ofset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                PaymentHistory history = new PaymentHistory();

                history.setTransactionId(resultSet.getInt("transaction_ID"));
                history.setAccountNumber(resultSet.getLong("account_number"));
                history.setTransactionAmount(resultSet.getBigDecimal("transaction_amount"));
                history.setCurrentBalance(resultSet.getBigDecimal("current_balance"));
                history.setDateOfTransaction(resultSet.getTimestamp("date_of_transaction").toLocalDateTime());
                history.setNotification(resultSet.getString("notification"));

                result.add(history);
            }
        }
        return result;
    }

    @Override
    public int getTotalCountOfRecords(Connection connection) throws SQLException {
        int countOfRecords = 0;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_TOTAL_COUNT_OF_RECORDS);
            while (resultSet.next()) {
                countOfRecords = resultSet.getInt(1);
            }
        }
        return countOfRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(GET_TOTAL_COUNT_OF_RECORDS, INSERT_NEW_ACTION_NOTE, GET_ALL_ACTION_FOR_CURRENT_ACC);
    }
}
