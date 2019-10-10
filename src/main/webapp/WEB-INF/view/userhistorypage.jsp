<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 15/09/2019
  Time: 18:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/mytag.tld" prefix="customTag" %>
<fmt:setLocale value="${sessionScope.currentLang}"/>
<fmt:setBundle basename="pages/userhistorypage"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:message key="account.number"/>
<c:out value="${sessionScope.accountNumber}"/>
<br>
<br>
<form action="${pageContext.request.contextPath}workwithaccount" method="post">
    <label>
        <input name="typeofwork" type="radio" value="addmoney" required checked>
    </label>
    <fmt:message key="addmoney"/>
    <br>
    <label>
        <input name="typeofwork" type="radio" value="sendmoney" required>
    </label>
    <fmt:message key="sending.money"/>
    <br>
    <input type="submit" value="<fmt:message key="button.workwithacc"/>">
</form>
<br>
<table>
    <tr>
        <th><fmt:message key="acc.date.of.transaction"/></th>
        <th><fmt:message key="acc.transaction.amount"/></th>
        <th><fmt:message key="acc.current.balance"/></th>
        <th><fmt:message key="acc.notification"/></th>
    </tr>
    <c:forEach items="${requestScope.history}" var="acc">
        <tr>

            <td><customTag:customDate localDateTime="${acc.dateOfTransaction}"/></td>
            <td>${acc.transactionAmount}</td>
            <td>${acc.currentBalance}</td>
            <td>${acc.notification}</td>
        </tr>
    </c:forEach>
</table>
<br>
<c:if test="${requestScope.currentPage != 1}">
    <td><a href="accountoperation?page=${requestScope.currentPage - 1}"><fmt:message key="previous"/></a></td>
</c:if>

<table>
    <tr>
        <c:forEach begin="1" end="${requestScope.numberOfPage}" var="i">
            <c:choose>
                <c:when test="${requestScope.currentPage eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <td><a href="accountoperation?page=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<c:if test="${requestScope.currentPage lt requestScope.numberOfPage}">
    <td><a href="accountoperation?page=${requestScope.currentPage + 1}"><fmt:message key="next"/></a></td>
</c:if>
<br>
<c:out value="${requestScope.exceptionError}"/>
<br>
<br>
<jsp:include page="logout.jsp"/>
</body>
</html>
