package ua.vlasovEugene.servletBankSystem.controller;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.service.RequestService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class GetAdminPage implements Command {
    private final RequestService service;
    private final static Logger LOG = Logger.getLogger(GetAdminPage.class);
    private final String ADMINPAGE =  "/WEB-INF/view/adminpage.jsp";

    public GetAdminPage() {
        service = RequestService.getInstance();
    }

    public GetAdminPage(RequestService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("allUserRequests", service.getAllRequests());
        request.getRequestDispatcher(ADMINPAGE).forward(request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetAdminPage that = (GetAdminPage) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
