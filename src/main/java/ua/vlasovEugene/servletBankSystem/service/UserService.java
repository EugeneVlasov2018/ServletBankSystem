package ua.vlasovEugene.servletBankSystem.service;

import ua.vlasovEugene.servletBankSystem.dao.*;
import ua.vlasovEugene.servletBankSystem.dao.daoFactory.AbstractDaoFactory;
import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.PaymentHistory;
import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.utils.TransactionHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.AccountNumberGenerator;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.PasswordGenerator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserService {
    private final AbstractDaoFactory FACTORY = AbstractDaoFactory.getDaoFactory("MY_SQL_FACTORY");
    private final BigDecimal DEPOSIT_INTEREST_RATE = new BigDecimal("5");
    private final String FIRST_NOTE_FOR_PAYMENT_HISTORY = "Your account was created, congratulations !";
    private final String USER_ROLE = "user";
    private IAccountDao accountDao;
    private IUserDao userDao;
    private IAccountHistoryDao accHistoryDao;
    private ICreditRequestDao creditReqDao;

    public UserService() {
        accountDao = FACTORY.getAccountDao();
        userDao = FACTORY.getUserDao();
        accHistoryDao = FACTORY.getAccountHistoryDao();
        creditReqDao = FACTORY.getRequestDao();
    }

    public UserService(IAccountDao accountDao, IUserDao userDao, IAccountHistoryDao accHistoryDao,
                       ICreditRequestDao creditReqDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.accHistoryDao = accHistoryDao;
        this.creditReqDao = creditReqDao;
    }

    public boolean currentUSerIsExist(String login, String password) throws DaoException {
        AtomicBoolean result = new AtomicBoolean(false);

        TransactionHandler.runInTransaction(connection -> {
            result.set(userDao.userByEmailIsExist(connection,login,password));
        });

        return result.get();
    }

    public Map<String,List<Account>> getAllCurrentUserAccounts(User currentUser) throws DaoException {
        AtomicReference<Map<String, List<Account>>> result = new AtomicReference<>();

        TransactionHandler.runInTransaction(connection -> {
            result.set(accountDao.getAllAccountsOfCurrentUser(connection, currentUser));
        });

        return result.get();
    }

    public void addNewUser(Map<String,String> parameters) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            BigDecimal firstPal = new BigDecimal(parameters.get("deposit").replace(',', '.'));

            User newUser = createNewUser(parameters.get("firstname"),
                    parameters.get("lastname"),
                    parameters.get("login"),
                    parameters.get("password"));

            Account newAccount = createNewDepositAccount(connection,
                    parameters.get("login"),
                    firstPal);

            PaymentHistory firstAction = createFirstAction(newAccount.getAccountNumber(),
                    firstPal);

            userDao.addNewUser(connection,newUser);
            accountDao.addNewAccount(connection,newAccount);
            accHistoryDao.addNewActionWithAccount(connection,firstAction);
        });
    }

    private PaymentHistory createFirstAction(long accountNumber, BigDecimal firstpal) {
        LocalDateTime currentDate = LocalDateTime.now();

        PaymentHistory result = new PaymentHistory();
        result.setAccountNumber(accountNumber);
        result.setTransactionAmount(firstpal);
        result.setCurrentBalance(firstpal);
        result.setDateOfTransaction(currentDate);
        result.setNotification(FIRST_NOTE_FOR_PAYMENT_HISTORY);

        return result;
    }

    private Account createNewDepositAccount(Connection connection, String login, BigDecimal firstpal) throws SQLException {
        LocalDateTime currentDate = LocalDateTime.now().plusYears(1);

        Account result = new Account();
        result.setAccountOwner(login);
        result.setAccountNumber(AccountNumberGenerator.getAccountNumber(connection, accountDao));
        result.setAccountType("deposit");
        result.setDeposit(firstpal);
        result.setCurrentBalance(firstpal);
        result.setInterestRate(DEPOSIT_INTEREST_RATE);
        result.setCreditLimit(new BigDecimal("0"));
        result.setAccountValidity(currentDate);

        return result;
    }

    private User createNewUser(String firstname, String lastname, String login, String password) {
        Map<String, String> passAndSalt = PasswordGenerator.createPassword(password);

        return User.newBuilder()
                .setUserFirstname(firstname)
                .setUserLastname(lastname)
                .setUserLoginEmail(login)
                .setUserPassword(passAndSalt.get("password"))
                .setUserRole(USER_ROLE)
                .setUserSalt(passAndSalt.get("salt"))
                .setUserHaveCreditAcc(false)
                .setCreditRequestStatus(false)
                .build();
    }

    public BigDecimal getTotalBalanceAfterHalfYear(User user, LocalDateTime afterSixMonts) throws DaoException {
        AtomicReference<BigDecimal> result = new AtomicReference<>();

        TransactionHandler.runInTransaction(connection -> {
            result.set(accountDao.getTotalUsersBalanceAfter6Year(connection, user, afterSixMonts));
        });

        return result.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserService that = (UserService) o;
        return Objects.equals(FACTORY, that.FACTORY) &&
                Objects.equals(DEPOSIT_INTEREST_RATE, that.DEPOSIT_INTEREST_RATE) &&
                Objects.equals(accountDao, that.accountDao) &&
                Objects.equals(userDao, that.userDao) &&
                Objects.equals(accHistoryDao, that.accHistoryDao) &&
                Objects.equals(creditReqDao, that.creditReqDao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FACTORY, DEPOSIT_INTEREST_RATE,
                FIRST_NOTE_FOR_PAYMENT_HISTORY, accountDao, userDao,
                accHistoryDao, creditReqDao);
    }
}
