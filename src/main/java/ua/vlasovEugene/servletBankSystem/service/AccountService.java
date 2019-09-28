package ua.vlasovEugene.servletBankSystem.service;

import ua.vlasovEugene.servletBankSystem.dao.IAccountDao;
import ua.vlasovEugene.servletBankSystem.dao.IAccountHistoryDao;
import ua.vlasovEugene.servletBankSystem.dao.IUserDao;
import ua.vlasovEugene.servletBankSystem.dao.daoFactory.AbstractDaoFactory;
import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.PaymentHistory;
import ua.vlasovEugene.servletBankSystem.utils.TransactionHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.AccountNumberGenerator;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AccountService {
    private final AbstractDaoFactory FACTORY = AbstractDaoFactory.getDaoFactory("MY_SQL_FACTORY");
    private IUserDao userDao;
    private IAccountDao accountDao;
    private IAccountHistoryDao historyDao;
    private final String REFILL_ACCOUNT = "Account replenishment";

    public AccountService() {
        userDao = FACTORY.getUserDao();
        accountDao = FACTORY.getAccountDao();
        historyDao = FACTORY.getAccountHistoryDao();
    }

    public AccountService(IUserDao userDao,IAccountDao dao, IAccountHistoryDao historyDao) {
        this.userDao = userDao;
        this.accountDao = dao;
        this.historyDao = historyDao;
    }

    public void createNewDepositAccount(BigDecimal initialFee, String userLogin) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            Account newAccount = new Account(
                    null,
                    userLogin,
                    AccountNumberGenerator.getAccountNumber(connection, accountDao),
                    "deposit",
                    initialFee,
                    new BigDecimal("5"),
                    new BigDecimal("0"),
                    LocalDateTime.now().plusYears(1),
                    initialFee
            );

            PaymentHistory firstAction = new PaymentHistory(
                    null,
                    newAccount.getAccountNumber(),
                    newAccount.getCurrentBalance(),
                    newAccount.getCurrentBalance(),
                    newAccount.getAccountValidity().minusYears(1),
                    "You have successfully created a new current account!"
            );

            accountDao.addNewAccount(connection, newAccount);
            historyDao.addNewActionWithAccount(connection, firstAction);

        });
    }

    public List<PaymentHistory> getHistoryOfCurrentAccount(Long accountNumber, int ofset, int limit) throws DaoException {
        AtomicReference<List<PaymentHistory>> accountHistory = new AtomicReference<>();

        TransactionHandler.runInTransaction(connection ->
                accountHistory.set(historyDao.getHistoryOfCurrentAccount(connection, accountNumber, ofset, limit))
        );

        return accountHistory.get();
    }


    public void refillCurrentAccount(Long accountNumber, BigDecimal summ) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {

            Account currentAccount = accountDao.getCurrentAccount(connection,accountNumber);
            BigDecimal currentBalance = currentAccount.getCurrentBalance().add(summ);

            accountDao.changeBalanceOfCurrentAccount(connection, accountNumber,currentBalance);
            LocalDateTime currentDate = LocalDateTime.now();

            PaymentHistory newAction = new PaymentHistory();
            newAction.setCurrentBalance(currentBalance);
            newAction.setAccountNumber(accountNumber);
            newAction.setTransactionAmount(summ);
            newAction.setNotification(REFILL_ACCOUNT);
            newAction.setDateOfTransaction(currentDate);

            historyDao.addNewActionWithAccount(connection,newAction);
        });
    }

    public boolean recipientAccountIsExist(Long accountNumber) throws DaoException {
        AtomicBoolean result = new AtomicBoolean(false);

        TransactionHandler.runInTransaction(connection -> {
            if (accountDao.getCurrentAccount(connection, accountNumber) != null)
                result.set(true);
        });

        return result.get();
    }

    public boolean currentAccountHaveEnoughMoney(Long currentAccount, BigDecimal countOfMoney) throws DaoException {
        AtomicBoolean result = new AtomicBoolean(false);

        TransactionHandler.runInTransaction(connection -> {
            Account account = accountDao.getCurrentAccount(connection,currentAccount);

            result.set(account.getCurrentBalance()
                    .add(countOfMoney)
                    .compareTo(account.getCreditLimit()) >= 0);

        });
        return result.get();
    }

    public void sendMoneyToAcc(Long donorAccNumber, Long recipientAccNumber, BigDecimal countOfMoney) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {

            Account donorAcc = accountDao.getCurrentAccount(connection,donorAccNumber);
            Account recipientAcc = accountDao.getCurrentAccount(connection,recipientAccNumber);

            BigDecimal donorBalance = donorAcc.getCurrentBalance().subtract(countOfMoney);
            BigDecimal recipientBalance = recipientAcc.getCurrentBalance().add(countOfMoney);

            accountDao.changeBalanceOfCurrentAccount(connection,donorAccNumber,donorBalance);
            accountDao.changeBalanceOfCurrentAccount(connection,recipientAccNumber,recipientBalance);

            PaymentHistory forDonorAcc = new PaymentHistory(
                    null,
                    donorAccNumber,
                    countOfMoney.negate(),
                    donorBalance,
                    LocalDateTime.now(),
                    String.format("Transfer to account No%s",recipientAccNumber)

            );

            PaymentHistory forRecipAcc = new PaymentHistory(
                    null,
                    recipientAccNumber,
                    countOfMoney,
                    recipientBalance,
                    LocalDateTime.now(),
                    String.format("Transfer from account No%s",donorAccNumber)
            );

            historyDao.addNewActionWithAccount(connection,forDonorAcc);
            historyDao.addNewActionWithAccount(connection,forRecipAcc);
        });
    }

    public void sendMoneyToAcc(Long donorAccNumber, String dummyRecipient, BigDecimal countOfMoney) throws DaoException {
        TransactionHandler.runInTransaction(connection -> {

            Account donorAcc = accountDao.getCurrentAccount(connection,donorAccNumber);

            BigDecimal donorBalance = donorAcc.getCurrentBalance().subtract(countOfMoney);

            accountDao.changeBalanceOfCurrentAccount(connection,donorAccNumber,donorBalance);

            PaymentHistory forDonorAcc = new PaymentHistory(
                    null,
                    donorAccNumber,
                    countOfMoney.negate(),
                    donorBalance,
                    LocalDateTime.now(),
                    String.format("Transfer to account No%s to another bank",dummyRecipient)

            );
            historyDao.addNewActionWithAccount(connection,forDonorAcc);
        });
    }

    public int getNumberOfRecord() throws DaoException {
        AtomicInteger countOfRecords = new AtomicInteger(0);

        TransactionHandler.runInTransaction(connection -> {
            countOfRecords.set(historyDao.getTotalCountOfRecords(connection));
        });

        return countOfRecords.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountService that = (AccountService) o;
        return FACTORY.equals(that.FACTORY) &&
                Objects.equals(userDao, that.userDao) &&
                Objects.equals(accountDao, that.accountDao) &&
                Objects.equals(historyDao, that.historyDao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FACTORY, userDao, accountDao, historyDao);
    }
}
