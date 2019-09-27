package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The interface Command.
 */
public interface Command {
    /**
     * Execute.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     * @throws DaoException     the dao exception
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, DaoException;
}
