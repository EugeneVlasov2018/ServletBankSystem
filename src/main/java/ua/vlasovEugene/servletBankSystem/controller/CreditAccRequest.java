package ua.vlasovEugene.servletBankSystem.controller;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.service.UserService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreditAccRequest implements Command {
    private final UserService service;
    private static final Logger LOG = Logger.getLogger(CreditAccRequest.class);

    public CreditAccRequest() {
        service = UserService.getInstance();
    }

    public CreditAccRequest(UserService service) {
        this.service = service;
    }

    private final String CREDIT_REQUEST_PAGE = "/WEB-INF/view/creditrequestform.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LocalDateTime dataAfterSix = LocalDateTime.now().plusMonths(6);

        request.setAttribute("summaryBalance", service.getTotalBalanceAfterHalfYear(
                (User) request.getSession().getAttribute("user"), dataAfterSix));

        LOG.info("Add 'summary deposit balance' to request");

        request.setAttribute("afterHalfYearData", dataAfterSix.toLocalDate());
        LOG.info(String.format("Add 'afterHalfYearData' = %s to request", dataAfterSix.toLocalDate()));


        LOG.info("do forward to page " + CREDIT_REQUEST_PAGE);
        request.getRequestDispatcher(CREDIT_REQUEST_PAGE).forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditAccRequest that = (CreditAccRequest) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
