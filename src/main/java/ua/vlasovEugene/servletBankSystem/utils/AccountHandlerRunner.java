package ua.vlasovEugene.servletBankSystem.utils;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.service.InternalAccountHandler;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.PasswordException;


public class AccountHandlerRunner extends Thread {
    private final static Logger LOG = Logger.getLogger(AccountHandlerRunner.class);
    private final InternalAccountHandler handler;
    private final Long SLEEP_ONE_DAY = (long) (1000 * 60 * 60 * 24);

    public AccountHandlerRunner() {
        handler = InternalAccountHandler.getInstance();
        start();
        LOG.info("AccountHandlerRunner successfull run");
    }

    @Override
    public void run() {
        while (true) {
            try {
                handler.doWork();
                Thread.sleep(SLEEP_ONE_DAY);
                LOG.info("handler finish all work and go sleep for 1 day");
            } catch (InterruptedException e) {
                LOG.error("get exception in AccountHandlerRunner", e);
                throw new PasswordException(e);
            }
        }
    }
}
