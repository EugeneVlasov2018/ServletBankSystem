package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.service.AccountService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class DepositAccCreator implements ua.vlasovEugene.servletBankSystem.controller.Command {
    private final AccountService service;
    private final String TARGETPAGE = "/userpage";

    public DepositAccCreator() {
        service = AccountService.getInstance();
    }

    public DepositAccCreator(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BigDecimal initialFee = new BigDecimal(request.getParameter("deposit").
                replace(",", "."));
        User currentUser = (User) request.getSession().getAttribute("user");
        service.createNewDepositAccount(initialFee, currentUser.getUserLoginEmail());
        response.sendRedirect(TARGETPAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositAccCreator that = (DepositAccCreator) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
