package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.service.RequestService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class CreditRequestDeleter implements Command {
    private final String TARGETPAGE = "/adminpage";
    private final RequestService service;

    public CreditRequestDeleter() {
        service = RequestService.getInstance();
    }

    public CreditRequestDeleter(RequestService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer requestId = Integer.valueOf(request.getParameter("requestId"));

        service.deleteCreditRequest(requestId);
        response.sendRedirect(TARGETPAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditRequestDeleter that = (CreditRequestDeleter) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
