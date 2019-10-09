package ua.vlasovEugene.servletBankSystem.utils.generators;

import ua.vlasovEugene.servletBankSystem.dao.IAccountDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class AccountNumberGenerator {
    public static Long getAccountNumber(Connection connection, IAccountDao accountDao) throws SQLException {
        List<Long> allAccountsNumbers = accountDao.getAllAccountNumbers(connection);
        int endOfRange = 999999999;
        int startOfRange = 99999999;

        Random random = new Random();
        int range = endOfRange - startOfRange + 1;
        long accountNumber = random.nextInt(range) + 1;

        while (allAccountsNumbers.contains(accountNumber)) {
            accountNumber = random.nextInt(range) + 1;
        }
        return accountNumber;
    }
}
