package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.entity.Account;
import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.service.UserService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GetUserPage implements Command {
    private final UserService service;
    private final String USERPAGE = "/WEB-INF/view/userpage.jsp";

    public GetUserPage() {
        service = UserService.getInstance();
    }

    public GetUserPage(UserService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, List<Account>> accounts;
        User currentUser = (User) request.getSession().getAttribute("user");
        accounts = service.getAllCurrentUserAccounts(currentUser);

        request.setAttribute("depositAccounts", accounts.get("depositAccounts"));
        request.setAttribute("creditAccounts", accounts.get("creditAccounts"));
        request.getRequestDispatcher(USERPAGE).forward(request,response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetUserPage that = (GetUserPage) o;
        return Objects.equals(USERPAGE, that.USERPAGE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(USERPAGE);
    }
}
