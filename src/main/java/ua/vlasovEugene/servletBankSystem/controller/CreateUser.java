package ua.vlasovEugene.servletBankSystem.controller;

import org.apache.log4j.Logger;
import ua.vlasovEugene.servletBankSystem.service.*;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateUser implements Command {
    private UserService userService;
    private static final Logger LOG = Logger.getLogger(CreateUser.class);
    private final String NEW_USER_PAGE = "/WEB-INF/view/newuser.jsp";

    public CreateUser() {
        userService = new UserService();
    }

    public CreateUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException {
        String login = request.getParameter("email");
        String password = request.getParameter("password");

        LOG.info("get login and password from request");


        if (!(userService.currentUSerIsExist(login, password))) {
            Map<String, String> parameters = new HashMap<>();

            parameters.put("login", login);
            parameters.put("password", password);
            parameters.put("firstname", request.getParameter("firstname"));
            parameters.put("lastname", request.getParameter("lastname"));
            parameters.put("deposit", request.getParameter("deposit"));

            LOG.info(String.format("A Map with value: %s, %s, %s, %s, %s was created",
                    parameters.get("login"), parameters.get("password"), parameters.get("firstname"),
                    parameters.get("lastname"), parameters.get("deposit")));

            userService.addNewUser(parameters);

            request.setAttribute("successOperation", true);
            LOG.info("parameter 'successOperation' is true");
        } else {
            request.setAttribute("userisexist", true);
            LOG.info("parameter 'userisexist' is true");
        }

        LOG.info("forvard to " + NEW_USER_PAGE);
        request.getRequestDispatcher(NEW_USER_PAGE).forward(request, response);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUser that = (CreateUser) o;
        return Objects.equals(userService, that.userService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService);
    }
}
