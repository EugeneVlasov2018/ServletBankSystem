package ua.vlasovEugene.servletBankSystem.controller;

import ua.vlasovEugene.servletBankSystem.service.AccountService;
import ua.vlasovEugene.servletBankSystem.utils.exceptions.DaoException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class MoneySender implements Command {
    private final AccountService service;

    private final String ERROR_PAGE = "/WEB-INF/view/currentAccOperation.jsp";

    public MoneySender() {
        service = AccountService.getInstance();
    }

    public MoneySender(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long currentAccount = Long.valueOf((String) request.getSession().getAttribute("accountNumber"));
        BigDecimal countOfMoney = new BigDecimal(request.getParameter("money")
                .replace(',', '.'))
                .negate();
        Long recipientAccountNumber = Long.valueOf(request.getParameter("account"));
        boolean intrabankOperation = request.getParameter("typeOfTransaction").equals("currentBank");

        if (service.currentAccountHaveEnoughMoney(currentAccount, countOfMoney)) {

            doTransaction(request, response, currentAccount, countOfMoney,
                    recipientAccountNumber, intrabankOperation);

        } else {
            request.setAttribute("noEnoughMoney", true);
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    private void doTransaction(HttpServletRequest request, HttpServletResponse response, Long currentAccount, BigDecimal countOfMoney,
                               Long recipientAccountNumber, boolean intrabankOperation) throws DaoException, ServletException, IOException {

        if (intrabankOperation) {
            if (service.recipientAccountIsExist(recipientAccountNumber)) {
                service.sendMoneyToAcc(currentAccount, recipientAccountNumber, countOfMoney);
                response.sendRedirect("/userpage/accountoperation");
            } else {
                request.setAttribute("noExistAccount", true);
                request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
            }
        } else {
            String dummyRecipient = String.valueOf(recipientAccountNumber);
            service.sendMoneyToAcc(currentAccount, dummyRecipient, countOfMoney);
            response.sendRedirect("/userpage/accountoperation");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneySender that = (MoneySender) o;
        return Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service);
    }
}
