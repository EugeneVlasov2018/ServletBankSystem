
<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 21/08/2019
  Time: 23:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/mytag.tld" prefix="customTag" %>
<fmt:setLocale value="${sessionScope.currentLang}"/>
<fmt:setBundle basename="pages/userpage"/>

<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:message key="greeting.user"/> <!--Обьеденить 2 тега в1 кастомный!!!-->
<c:out value="${sessionScope.user.userFirstname} ${sessionScope.user.userLastname}))"/>
<br>
<br>
<c:if test="${empty requestScope.depositAccounts}">
    <fmt:message key="no.accounts"/>
</c:if>
<br>
<form action="${pageContext.request.contextPath}userpage/createdepositeacc" method="post">
    <input type="submit" value="<fmt:message key="create.new.acc"/> ">
</form>
<fmt:message key="customer.deposit.accounts"/><br>
<table>
    <tr>
        <th><fmt:message key="account.number"/></th>
        <th><fmt:message key="current.balance"/></th>
        <th><fmt:message key="deposit"/></th>
        <th><fmt:message key="interest.rate"/></th>
        <th><fmt:message key="account.validity"/></th>
    </tr>
    <c:forEach items="${requestScope.depositAccounts}" var="depAcc">
        <tr>
            <td>${depAcc.accountNumber}</td>
            <td>${depAcc.currentBalance}</td>
            <td>${depAcc.deposit}</td>
            <td>${depAcc.interestRate}</td>
            <td>
                <fmt:message key="account.validity.message"/><br>
                <customTag:customDate localDateTime="${depAcc.accountValidity}"/>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}userpage/accountoperation" method="post">
                    <input name="accountNumber" type="hidden" value="${depAcc.accountNumber}">
                    <input type="submit" value="<fmt:message key="account.details"/>"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<br>
<c:if test="${sessionScope.user.creditRequestStatus==true}">
    <fmt:message key="credit.request.sended"/>
</c:if>
<c:if test="${empty requestScope.creditAccounts && sessionScope.user.creditRequestStatus==false}">
    <form action="${pageContext.request.contextPath}userpage/createcreditacc" method="get">
        <input type="submit" value="<fmt:message key="button.account.request"/>"/>
    </form>
</c:if>
<c:if test="${not empty requestScope.creditAccounts}">
    <fmt:message key="customer.credit.accounts"/><br>
    <table>
        <tr>
            <th><fmt:message key="account.number"/></th>
            <th><fmt:message key="current.balance"/></th>
            <th><fmt:message key="limit.for.credit"/></th>
            <th><fmt:message key="interest.rate"/></th>
            <th><fmt:message key="account.validity"/></th>
        </tr>
        <c:forEach items="${requestScope.creditAccounts}" var="credAcc">
            <tr>
                <td>${credAcc.accountNumber}</td>
                <td>${credAcc.currentBalance}</td>
                <td>${credAcc.creditLimit}</td>
                <td>${credAcc.interestRate}</td>
                <td>
                    <fmt:message key="account.validity.message"/><br>
                    <customTag:customDate localDateTime="${credAcc.accountValidity}"/>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}userpage/accountoperation" method="post">
                        <input name="accountNumber" type="hidden" value="${credAcc.accountNumber}">
                        <input type="submit" value="<fmt:message key="account.details"/>"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<br>
<br>
<jsp:include page="logout.jsp"/>
</body>
</html>
