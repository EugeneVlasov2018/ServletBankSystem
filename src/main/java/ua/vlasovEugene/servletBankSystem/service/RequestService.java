package ua.vlasovEugene.servletBankSystem.service;

import ua.vlasovEugene.servletBankSystem.dao.IAccountDao;
import ua.vlasovEugene.servletBankSystem.dao.ICreditRequestDao;
import ua.vlasovEugene.servletBankSystem.dao.IUserDao;
import ua.vlasovEugene.servletBankSystem.dao.daoFactory.AbstractDaoFactory;
import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.CreditOpeningRequest;
import ua.vlasovEugene.servletBankSystem.utils.TransactionHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.AccountNumberGenerator;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RequestService{

    private final AbstractDaoFactory FACTORY = AbstractDaoFactory.getDaoFactory("MY_SQL_FACTORY");
    private ICreditRequestDao requestDao;
    private IUserDao userDao;
    private IAccountDao accountDao;

    public RequestService() {
        requestDao = FACTORY.getRequestDao();
        userDao = FACTORY.getUserDao();
        accountDao = FACTORY.getAccountDao();
    }

    public RequestService(ICreditRequestDao requestDao, IUserDao userDao, IAccountDao accountDao) {
        this.requestDao = requestDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    public List<CreditOpeningRequest> getAllRequests() throws DaoException {
        AtomicReference<List<CreditOpeningRequest>> requests = new AtomicReference<>();

        TransactionHandler.runInTransaction(connection -> requests.set(requestDao.getAllUserRequests(connection)));

        return requests.get();
    }

    public void createNewCreditRequest(String userLoginEmail, BigDecimal totalUserBalance,
                                       BigDecimal creditLimit, LocalDateTime dataOfTotalBalance) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            CreditOpeningRequest currentRequest = new CreditOpeningRequest(
                    null,
                    userLoginEmail,
                    totalUserBalance,
                    creditLimit,
                    dataOfTotalBalance
            );
            requestDao.addNewRequest(connection, currentRequest);
            userDao.changeCreditRequestStatusOfCurrentUser(connection, userLoginEmail, true);
        });
    }

    public void createNewCreditAcc(Integer requestId) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            CreditOpeningRequest currentRequest = requestDao.getRequestById(connection, requestId);
            Account newCreditAcc = getCreditAcc(connection, currentRequest);
            accountDao.addNewAccount(connection, newCreditAcc);
            requestDao.deleteCurrentRequest(connection, currentRequest);
            userDao.changeCreditRequestStatusOfCurrentUser(
                    connection, currentRequest.getUserEmailLogin(), false);
            userDao.changeCreditStatusOfCurrentUser(
                    connection, currentRequest.getUserEmailLogin(), true);

        });
    }

    public void deleteCreditRequest(Integer requestId) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            CreditOpeningRequest currentRequest = requestDao.getRequestById(connection, requestId);
            requestDao.deleteCurrentRequest(connection, currentRequest);
            userDao.changeCreditRequestStatusOfCurrentUser(
                    connection, currentRequest.getUserEmailLogin(), false);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestService that = (RequestService) o;
        return Objects.equals(FACTORY, that.FACTORY) &&
                Objects.equals(requestDao, that.requestDao) &&
                Objects.equals(userDao, that.userDao) &&
                Objects.equals(accountDao, that.accountDao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FACTORY, requestDao, userDao, accountDao);
    }

    private Account getCreditAcc(Connection connection, CreditOpeningRequest currentRequest) throws SQLException {
        BigDecimal creditLimit = currentRequest.getExpectedCreditLimit().negate();

        return new Account(
                null,
                currentRequest.getUserEmailLogin(),
                AccountNumberGenerator.getAccountNumber(connection, accountDao),
                "credit",
                new BigDecimal("0"),
                new BigDecimal("10"),
                creditLimit,
                currentRequest.getDateOfEndCredit(),
                new BigDecimal(0)
        );
    }

}
