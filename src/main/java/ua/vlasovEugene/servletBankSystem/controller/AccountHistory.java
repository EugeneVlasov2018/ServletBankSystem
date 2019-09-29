package ua.vlasovEugene.servletBankSystem.controller;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.PaymentHistory;
import ua.vlasovEugene.servletBankSystem.service.AccountService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AccountHistory implements Command {
    private final AccountService service;
    private final String ACOOUNT_PAGE = "/WEB-INF/view/userhistorypage.jsp";
    private static final Logger LOG = Logger.getLogger(AccountHistory.class);


    public AccountHistory() {
        service = AccountService.getInstance();
    }

    public AccountHistory(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long accountNumber = getNumberOfCurrentAcc(request);
        preparePage(request, accountNumber);

        LOG.info("do forward to page " + ACOOUNT_PAGE);

        request.getRequestDispatcher(ACOOUNT_PAGE).forward(request, response);
    }

    private Long getNumberOfCurrentAcc(HttpServletRequest request) {
        if (request.getParameter("accountNumber") != null) {

            Long accountNumber = Long.valueOf(request.getParameter("accountNumber"));
            request.getSession().setAttribute("accountNumber", String.valueOf(accountNumber));
            return accountNumber;

        } else {
            return Long.valueOf((String) request.getSession().getAttribute("accountNumber"));
        }
    }

    private void preparePage(HttpServletRequest request, Long accountNumber) {
        int page = 1;
        int recordsPerPage = 5;
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        List<PaymentHistory> history = service.getHistoryOfCurrentAccount(
                accountNumber, (page - 1) * recordsPerPage, recordsPerPage);

        int numberOfRecord = service.getNumberOfRecord();
        int numberOfPage = (int) Math.ceil(numberOfRecord * 1.0 / recordsPerPage);

        request.setAttribute("history", history);
        request.setAttribute("numberOfPage", numberOfPage);
        request.setAttribute("currentPage", page);

        LOG.info("Add list of history of current acc to request");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountHistory that = (AccountHistory) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
