package ua.vlasovEugene.servletBankSystem.utils;

import ua.vlasovEugene.servletBankSystem.service.InternalAccountHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;


public class AccountHandlerRunner extends Thread {
    private final InternalAccountHandler hadler;
    private final Long SLEEP_ONE_DAY = (long) (1000 * 60 * 60 * 24);

    public AccountHandlerRunner() {
        hadler = InternalAccountHandler.getInstance();
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                hadler.doWork();
                Thread.sleep(SLEEP_ONE_DAY);
            } catch (DaoException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
