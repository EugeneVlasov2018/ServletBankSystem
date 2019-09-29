package ua.vlasovEugene.servletBankSystem.utils.transaction;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


/**
 * Transactional class
 */
public class TransactionHandler {
    private static final Logger LOG = Logger.getLogger(TransactionHandler.class);
    /**
     * The purpose of the method is to unite the execution of all called
     * within the framework of a single transaction
     * @param transaction The functional interface in the method of which we will invest
     *                   all the methods that we want to execute in one transaction
     * @see Transaction
     * @throws DaoException if something goes wrong this exception will be thrown
     */
    public static void runInTransaction(Transaction transaction) {
        Connection connection  = null;

        try {
            connection = ConnectionPool.getConnection();
            LOG.info("get connection from ConnectionPool");
            connection.setAutoCommit(false);
            transaction.execute(connection);
            connection.commit();
            LOG.info("set falce autocommit, do some work, commit connection");
        } catch (SQLException e) {
            LOG.warn("something wrong!");
            try {
                if (connection != null) {
                    connection.rollback();
                    LOG.warn("rollback connection");
                }
            } catch (SQLException ex) {
                LOG.error("get exception", e);
                throw new DaoException(e);
            }
        } finally {
            try {
                Objects.requireNonNull(connection).setAutoCommit(true);
                connection.close();
                LOG.info("back connection to ConnectionPool");
            } catch (SQLException e) {
                LOG.error("get exception", e);
                throw new DaoException(e);
            }
        }
    }
}
