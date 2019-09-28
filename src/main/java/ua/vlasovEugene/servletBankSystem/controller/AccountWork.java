package ua.vlasovEugene.servletBankSystem.controller;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class AccountWork implements Command {
    private final AccountService service;
    private static final Logger LOG = Logger.getLogger(AccountWork.class);
    private final String ADD_MONEY_PAGE = "/WEB-INF/view/addmoneypage.jsp";
    private final String SEND_MONEY = "/WEB-INF/view/currentAccOperation.jsp";

    public AccountWork() {
        service = AccountService.getInstance();
    }

    public AccountWork(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String typeOfWork = request.getParameter("typeofwork");
        LOG.info("Get 'typeofwork' parameter from request: " + typeOfWork);

        switch (typeOfWork) {
            case "addmoney":
                LOG.info("go to page " + ADD_MONEY_PAGE);
                request.getRequestDispatcher(ADD_MONEY_PAGE).forward(request, response);
                break;
            case "sendmoney":
                LOG.info("go to page " + SEND_MONEY);
                request.getRequestDispatcher(SEND_MONEY).forward(request, response);
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountWork that = (AccountWork) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
