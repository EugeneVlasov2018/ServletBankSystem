package ua.vlasovEugene.servletBankSystem.service;

import ua.vlasovEugene.servletBankSystem.dao.IUserDao;
import ua.vlasovEugene.servletBankSystem.dao.daoFactory.AbstractDaoFactory;
import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.utils.TransactionHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AuthentificationService {
    private final AbstractDaoFactory FACTORY = AbstractDaoFactory.getDaoFactory("MY_SQL_FACTORY");
    private final IUserDao dao;

    private static volatile AuthentificationService instance;

    public static AuthentificationService getInstance() {
        if (instance == null)
            synchronized (AuthentificationService.class) {
                if (instance == null)
                    instance = new AuthentificationService();
            }
        return instance;
    }

    private AuthentificationService() {
        dao = FACTORY.getUserDao();
    }

    public AuthentificationService(IUserDao dao) {
        this.dao = dao;
    }

    public User getUserByLogin(String login) throws DaoException {
        AtomicReference<User> result = new AtomicReference<>();

        TransactionHandler.runInTransaction(connection -> result.set(dao.getUserByLogin(connection,login)));

        return result.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthentificationService service = (AuthentificationService) o;
        return Objects.equals(FACTORY, service.FACTORY) &&
                Objects.equals(dao, service.dao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FACTORY, dao);
    }
}
