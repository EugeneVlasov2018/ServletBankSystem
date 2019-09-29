package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.service.AccountService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class RefillAccount implements Command {
    private final AccountService service;
    private final String ACCOUNTPAGE = "/WEB-INF/view/userhistorypage.jsp";

    public RefillAccount(AccountService service) {
        this.service = service;
    }

    public RefillAccount() {
        service = AccountService.getInstance();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long accountNumber = Long.valueOf((String) request.getSession().getAttribute("accountNumber"));
        BigDecimal summ = new BigDecimal(request.getParameter("summ").replace(',', '.'));

        service.refillCurrentAccount(accountNumber, summ);

        response.sendRedirect("/userpage/accountoperation");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefillAccount that = (RefillAccount) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
