package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.entity.User;
import ua.vlasovEugene.servletBankSystem.service.AuthentificationService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.PasswordGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class LoginUser implements Command {
    private final String INDEXPAGE = "/WEB-INF/view/index.jsp";

    private AuthentificationService service;

    public LoginUser() {
        service = new AuthentificationService();
    }

    public LoginUser(AuthentificationService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DaoException {
        String login = request.getParameter("email");
        String password = request.getParameter("password");
        User user;

        user = service.getUserByLogin(login);
        System.out.println("from service " + user);

        if (user != null) {
            if (checkUser(request, password, user)) {
                forwardUserToPage(user, response);
                return;
            }
        } else {
            request.setAttribute("wrongLogin", true);
        }
        request.getRequestDispatcher(INDEXPAGE).forward(request, response);
    }

    private boolean checkUser(HttpServletRequest request, String password, User user) {
        if (PasswordGenerator.checkPassword(user, password)) {

            /* СУПЕРВАЖНО ОСТАВИТЬ ПРИ РЕФАКТОРИНГЕ, ЧТОБЫ НЕ ПУСКАТЬ В СЕССИЮ ЮЗЕРА С ПАРОЛЕМ И СОЛЬЮ!!!*/
            user.setUserPassword(null);
            user.setSalt(null);

            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            return true;

        }
        request.setAttribute("wrongPassword", true);
        return false;
    }

    private void forwardUserToPage(User userForSession, HttpServletResponse response) throws IOException {
        String userRole = userForSession.getUserRole();
        if(userRole.equalsIgnoreCase("user"))
            response.sendRedirect("/userpage");
        else if (userRole.equalsIgnoreCase("admin"))
            response.sendRedirect("/adminpage");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginUser loginUser = (LoginUser) o;
        return Objects.equals(service, loginUser.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(INDEXPAGE, service);
    }
}
