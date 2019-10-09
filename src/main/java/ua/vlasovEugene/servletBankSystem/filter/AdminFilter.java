package ua.vlasovEugene.servletBankSystem.filter;

import ua.vlasovEugene.servletBankSystem.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User userFromSession = (User) request.getSession().getAttribute("user");

        if(userFromSession.getUserRole().equalsIgnoreCase("admin")){
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            response.sendRedirect("/userpage");
        }
    }
}
