<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 19/09/2019
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.currentLang}"/>
<fmt:setBundle basename="pages/currentAccOperation"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <fmt:message key="send.money"/>
    <c:out value="${sessionScope.accountNumber}"/>
    <form action="${pageContext.request.contextPath}accountoperation" method="post">
        <label>
            <input hidden name="command" value="sendMoney">
        </label>
        <label>
            <input type="radio" name="typeOfTransaction" value="currentBank" checked>
            <fmt:message key="current.bank"/>
        </label>
        <br>
        <label>
            <input type="radio" name="typeOfTransaction" value="anotherbank">
            <fmt:message key="another.bank"/>
        </label>
        <br>
        <label>
            <input type="text" name="account" pattern="^[0-9]*[.,]?[0-9]+$"
                   placeholder="<fmt:message key="recipient.account"/>" required>
        </label>
        <c:if test="${requestScope.noExistAccount == true}">
            <fmt:message key="wrong.rec.acc"/>
        </c:if>
        <br>
        <label>
            <input type="text" name="money" pattern="^[0-9]*[.,]?[0-9]+$"
                   placeholder="<fmt:message key="money.count"/>" required>
        </label>
        <c:if test="${requestScope.noEnoughMoney == true}">
            <fmt:message key="dont.have.money"/>
        </c:if>
        <br>
        <input type="submit" value="<fmt:message key="button.sendmoney"/>">
    </form>
    <br>
    <br>
    <form action="${pageContext.request.contextPath}accountoperation" method="get">
        <input type="submit" value="<fmt:message key="back.acchisory"/> ">
    </form>
</body>
</html>
