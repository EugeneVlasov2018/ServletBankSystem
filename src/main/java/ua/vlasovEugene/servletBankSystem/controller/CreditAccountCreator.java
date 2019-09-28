package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.service.RequestService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class CreditAccountCreator implements Command {
    private final String TARGETPAGE = "/adminpage";
    private final RequestService service;

    public CreditAccountCreator() {
        service = RequestService.getInstance();
    }

    public CreditAccountCreator(RequestService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, DaoException {
        String requestId = request.getParameter("requestId");
        service.createNewCreditAcc(Integer.valueOf(requestId));
        response.sendRedirect(TARGETPAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditAccountCreator that = (CreditAccountCreator) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
