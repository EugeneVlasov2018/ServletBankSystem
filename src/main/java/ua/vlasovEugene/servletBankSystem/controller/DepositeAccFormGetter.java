package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class DepositeAccFormGetter implements ua.vlasovEugene.servletBankSystem.controller.Command {
    private final String TARGETPAGE = "/WEB-INF/view/newDepositAcc.jsp";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DaoException {
        request.getRequestDispatcher(TARGETPAGE).forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositeAccFormGetter that = (DepositeAccFormGetter) o;
        return Objects.equals(TARGETPAGE, that.TARGETPAGE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TARGETPAGE);
    }
}
