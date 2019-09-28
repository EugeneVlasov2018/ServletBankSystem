package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.service.RequestService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class CreditAccRequestCreator implements Command {
    private final RequestService service;
    private final String TARGETPAGE = "/userpage";

    public CreditAccRequestCreator() {
        service = RequestService.getInstance();
    }

    public CreditAccRequestCreator(RequestService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, DaoException {
        User requestSender = (User) request.getSession().getAttribute("user");

        sendCreditRequestToAdmin(request, requestSender);
        changeCreditReqStatusOfUserInSession(request, requestSender);

        response.sendRedirect(TARGETPAGE);
    }

    private void changeCreditReqStatusOfUserInSession(HttpServletRequest request, User requestSender) {
        requestSender.setCreditRequestStatus(true);
        request.getSession().setAttribute("user", requestSender);
    }

    private void sendCreditRequestToAdmin(HttpServletRequest request, User requestSender) throws DaoException {
        service.createNewCreditRequest(
                requestSender.getUserLoginEmail(),
                new BigDecimal(request.getParameter("totalUserBalance")),
                new BigDecimal(request.getParameter("creditLimit")),
                LocalDateTime.of(LocalDate.parse(request.getParameter("DataForCreditAcc")), LocalTime.now())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditAccRequestCreator that = (CreditAccRequestCreator) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, TARGETPAGE);
    }
}
