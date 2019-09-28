package ua.vlasovEugene.servletBankSystem.service;

import ua.vlasovEugene.servletBankSystem.dao.IAccountDao;
import ua.vlasovEugene.servletBankSystem.dao.IAccountHistoryDao;
import ua.vlasovEugene.servletBankSystem.dao.daoFactory.AbstractDaoFactory;
import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.PaymentHistory;
import ua.vlasovEugene.servletBankSystem.utils.TransactionHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class InternalAccountHandler {
    private final IAccountDao accountDao;
    private final IAccountHistoryDao historyDao;
    private final AbstractDaoFactory FACTORY = AbstractDaoFactory.getDaoFactory("MY_SQL_FACTORY");
    private final String NOTE_FOR_CREDIT_ACC = "Loan interest was deducted from your account";
    private final String NOTE_FOR_DEPOSIT_ACC = "You have been accrued interest on the deposit";


    private static volatile InternalAccountHandler instance;

    public static InternalAccountHandler getInstance() {
        if (instance == null)
            synchronized (InternalAccountHandler.class) {
                if (instance == null)
                    instance = new InternalAccountHandler();
            }
        return instance;
    }

    private InternalAccountHandler() {
        accountDao = FACTORY.getAccountDao();
        historyDao = FACTORY.getAccountHistoryDao();
    }

    public InternalAccountHandler(IAccountDao accountDao, IAccountHistoryDao historyDao) {
        this.accountDao = accountDao;
        this.historyDao = historyDao;
    }


    public void doWork() throws DaoException {
        TransactionHandler.runInTransaction(connection -> {
            List<Account> accounts = accountDao.getAllAccounts(connection);

            if (!accounts.isEmpty()) {

                for (Account currentAccount : accounts) {
                    BigDecimal newBalance = changeBalanceOfAcc(currentAccount);
                    PaymentHistory newAction = getPaymentHistory(currentAccount, newBalance);

                    accountDao.changeBalanceOfCurrentAccount(connection,
                            currentAccount.getAccountNumber(),
                            newBalance);

                    historyDao.addNewActionWithAccount(connection, newAction);
                }
            }
        });
    }

    private PaymentHistory getPaymentHistory(Account currentAccount, BigDecimal newBalance) {
        String notification;
        if (currentAccount.getAccountType().equalsIgnoreCase("credit"))
            notification = NOTE_FOR_CREDIT_ACC;
        else
            notification = NOTE_FOR_DEPOSIT_ACC;

        return new PaymentHistory(
                null,
                currentAccount.getAccountNumber(),
                newBalance.subtract(currentAccount.getCurrentBalance()),
                newBalance,
                LocalDateTime.now(),
                notification
        );
    }

    private BigDecimal changeBalanceOfAcc(Account currentAccount) throws DaoException {

        if (currentAccount.getAccountType().equalsIgnoreCase("credit")) {
            if (currentAccount.getCurrentBalance().doubleValue() < 0) {
                return getNewBalance(currentAccount.getCurrentBalance(),
                        currentAccount.getCreditLimit(),
                        currentAccount.getInterestRate());

            }
        } else {
            return getNewBalance(currentAccount.getCurrentBalance(),
                    currentAccount.getDeposit(),
                    currentAccount.getInterestRate());

        }
        throw new DaoException("This exception should never be thrown.");
    }

    private BigDecimal getNewBalance(BigDecimal balance, BigDecimal parameter, BigDecimal interestRate) {
        BigDecimal percent = interestRate.divide(new BigDecimal("365"), 10, RoundingMode.HALF_EVEN);

        return parameter
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN)
                .multiply(percent)
                .add(balance)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalAccountHandler that = (InternalAccountHandler) o;
        return Objects.equals(FACTORY, that.FACTORY) &&
                Objects.equals(accountDao, that.accountDao) &&
                Objects.equals(historyDao, that.historyDao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FACTORY, accountDao, historyDao);
    }
}
