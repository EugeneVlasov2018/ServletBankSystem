package ua.vlasovEugene.servletBankSystem.controller.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Exception handler servlet.
 */
public class ExceptionHandlerServlet extends HttpServlet {
    private final String ERRORPAGE = "/WEB-INF/webapp/errorpage.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        req.setAttribute("throwable", throwable);
        req.setAttribute("statusCode", statusCode);
        req.getRequestDispatcher(ERRORPAGE).forward(req, resp);
    }
}
