package ua.vlasovEugene.servletBankSystem.controller.servlets;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.controller.*;
import ua.vlasovEugene.servletBankSystem.service.InternalAccountHandler;
import ua.vlasovEugene.servletBankSystem.utils.AccountHandlerRunner;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The type Front controller.
 */
public class FrontController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(FrontController.class);

    @Override
    public void init() throws ServletException {
        AccountHandlerRunner runner = new AccountHandlerRunner();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doWork(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doWork(req,resp);
    }

    private void doWork(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command;
        if (req.getParameter("command") != null)
            command = req.getParameter("command");
        else {
            command = req.getRequestURI();
        }

        LOG.info(String.format("Command %s received", command));
        Command currentCommand = CommandContainer.getCommand(command);
        currentCommand.execute(req, resp);
    }
}
